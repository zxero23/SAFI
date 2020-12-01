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
import com.safi.entity.TipoImputacion;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@Stateless
public class TipoImputacionFacade extends AbstractFacade<TipoImputacion> implements TipoImputacionFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoImputacionFacade() {
        super(TipoImputacion.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoImputacion> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoImputacion as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoImputacion> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TipoImputacion as o WHERE o.estado = true AND o.nombre LIKE '%" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoImputacion> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoImputacion as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoImputacion as o WHERE o.estado = true ");
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
            TipoImputacion tipoImputacionAux = new TipoImputacion();
            tipoImputacionAux.setNombre(nombre.toUpperCase());
            tipoImputacionAux.setEstado(true);
            this.create(tipoImputacionAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el código de imputación");
        }
    }
    
    @Override
    public void edit(Long idTipoImputacion, String nombre) throws Exception {
        try {
            TipoImputacion tipoImputacionAux = this.find(idTipoImputacion);
            tipoImputacionAux.setNombre(nombre.toUpperCase());
            this.edit(tipoImputacionAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el código de imputación");
        }
    }
    
    @Override
    public void remove(Long idTipoImputacion) throws Exception {
        try {
            TipoImputacion tipoImputacionAux = this.find(idTipoImputacion);
            tipoImputacionAux.setEstado(false);
            this.edit(tipoImputacionAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el código de imputación");
        }
    }
    
}
