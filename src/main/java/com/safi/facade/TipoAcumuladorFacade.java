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
import com.safi.entity.TipoAcumulador;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, FacundoGonzalez
 */
@Stateless
public class TipoAcumuladorFacade extends AbstractFacade<TipoAcumulador> implements TipoAcumuladorFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoAcumuladorFacade() {
        super(TipoAcumulador.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoAcumulador> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoAcumulador as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoAcumulador> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TipoAcumulador as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombre).toUpperCase() + "%' ORDER BY o.id ").getResultList();
    }

    @Override
    public void create(String nombre) throws Exception {
        try {
            TipoAcumulador tipoAcumuladorAux = new TipoAcumulador();
            tipoAcumuladorAux.setNombre(nombre.toUpperCase());
            tipoAcumuladorAux.setEstado(true);
            this.create(tipoAcumuladorAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el acumulador");
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoAcumulador> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoAcumulador as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoAcumulador as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public void edit(Long idTipoAcumulador, String nombre) throws Exception {
        try {
            TipoAcumulador tipoAcumuladorAux = this.find(idTipoAcumulador);
            tipoAcumuladorAux.setNombre(nombre.toUpperCase());
            this.edit(tipoAcumuladorAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el acumulador");
        }
    }

    @Override
    public void remove(Long idTipoAcumulador) throws Exception {
        try {
            TipoAcumulador tipoAcumuladorAux = this.find(idTipoAcumulador);
            tipoAcumuladorAux.setEstado(false);
            this.edit(tipoAcumuladorAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el acumulador");
        }
    }

}
