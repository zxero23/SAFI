/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import com.safi.facade.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.TipoFondo;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@Stateless
public class TipoFondoFacade extends AbstractFacade<TipoFondo> implements TipoFondoFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoFondoFacade() {
        super(TipoFondo.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoFondo> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoFondo as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoFondo> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TipoFondo as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombre).toUpperCase() + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoFondo> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoFondo as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        query.append("ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM TipoFondo as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }
    
    @Override
    public void create(String nombre) throws Exception {
        try {
            TipoFondo tipoFondoAux = new TipoFondo();
            tipoFondoAux.setNombre(nombre.toUpperCase());
            tipoFondoAux.setEstado(true);
            this.create(tipoFondoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la clase de fondo.");
        }
    }
    
    @Override
    public void edit(Long idTipoFondo, String nombre) throws Exception {
        try {
            TipoFondo tipoFondoAux = this.find(idTipoFondo);
            tipoFondoAux.setNombre(nombre.toUpperCase());
            this.edit(tipoFondoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la clase de fondo.");
        }
    }
    
    @Override
    public void remove(Long idTipoFondo) throws Exception {
        try {
            TipoFondo tipoFondoAux = this.find(idTipoFondo);
            tipoFondoAux.setEstado(false);
            this.edit(tipoFondoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la clase de fondo.");
        }
    }
    
}