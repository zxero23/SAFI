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
import com.safi.entity.TipoMoneda;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@Stateless
public class TipoMonedaFacade extends AbstractFacade<TipoMoneda> implements TipoMonedaFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoMonedaFacade() {
        super(TipoMoneda.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoMoneda> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoMoneda as o WHERE o.estado = :p1 order by o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoMoneda> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TipoMoneda as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombre)+ "%' order by o.nombre").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoMoneda> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoMoneda as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoMoneda as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }
    
    @Override
    public void create(String nombre, String descripcion) throws Exception {
        try {
            TipoMoneda tipoMonedaAux = new TipoMoneda();
            tipoMonedaAux.setNombre(nombre.toUpperCase());
            tipoMonedaAux.setDescripcion(descripcion.toUpperCase());
            tipoMonedaAux.setEstado(true);
            this.create(tipoMonedaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el tipo de moneda");
        }
    }
    
    @Override
    public void edit(Long idTipoMoneda, String nombre, String descripcion) throws Exception {
        try {
            TipoMoneda tipoMonedaAux = this.find(idTipoMoneda);
            tipoMonedaAux.setNombre(nombre.toUpperCase());
            tipoMonedaAux.setDescripcion(descripcion.toUpperCase());
            this.edit(tipoMonedaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el tipo de moneda.");
        }
    }
    
    @Override
    public void remove(Long idTipoMoneda) throws Exception {
        try {
            TipoMoneda tipoMonedaAux = this.find(idTipoMoneda);
            tipoMonedaAux.setEstado(false);
            this.edit(tipoMonedaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el tipo de moneda.");
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoMoneda> findByName(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoMoneda as o WHERE o.estado = true");
        if (!(nombre.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '").append(Utilidad.desacentuar(nombre).toUpperCase()).append("'");
        }
        query.append(" ORDER BY o.nombre ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
}
