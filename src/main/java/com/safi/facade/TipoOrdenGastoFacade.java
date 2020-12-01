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
import com.safi.entity.TipoOrdenGasto;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@Stateless
public class TipoOrdenGastoFacade extends AbstractFacade<TipoOrdenGasto> implements TipoOrdenGastoFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoOrdenGastoFacade() {
        super(TipoOrdenGasto.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoOrdenGasto> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoOrdenGasto as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoOrdenGasto> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TipoOrdenGasto as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombre).toUpperCase() + "%' ORDER BY o.id ").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoOrdenGasto> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoOrdenGasto as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoOrdenGasto as o WHERE o.estado = true ");
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
            TipoOrdenGasto tipoOrdenGastoAux = new TipoOrdenGasto();
            tipoOrdenGastoAux.setNombre(nombre.toUpperCase());
            tipoOrdenGastoAux.setEstado(true);
            this.create(tipoOrdenGastoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el código de orden");
        }
    }
    
    @Override
    public void edit(Long idTipoOrdenGasto, String nombre) throws Exception {
        try {
            TipoOrdenGasto tipoOrdenGastoAux = this.find(idTipoOrdenGasto);
            tipoOrdenGastoAux.setNombre(nombre.toUpperCase());
            this.edit(tipoOrdenGastoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el código de orden");
        }
    }
    
    @Override
    public void remove(Long idTipoOrdenGasto) throws Exception {
        try {
            TipoOrdenGasto tipoOrdenGastoAux = this.find(idTipoOrdenGasto);
            tipoOrdenGastoAux.setEstado(false);
            this.edit(tipoOrdenGastoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el código de orden");
        }
    }
    
}
