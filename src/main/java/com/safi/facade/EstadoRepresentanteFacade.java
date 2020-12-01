/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.EstadoRepresentante;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class EstadoRepresentanteFacade extends AbstractFacade<EstadoRepresentante> implements EstadoRepresentanteFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoRepresentanteFacade() {
        super(EstadoRepresentante.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<EstadoRepresentante> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from EstadoRepresentante as o WHERE o.estado = :p1 order by o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<EstadoRepresentante> findAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM EstadoRepresentante as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        query.append("ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<EstadoRepresentante> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM EstadoRepresentante as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM EstadoRepresentante as o WHERE o.estado = true ");
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
            EstadoRepresentante estadoRepresentanteAux = new EstadoRepresentante();
            estadoRepresentanteAux.setNombre(nombre.toUpperCase());
            estadoRepresentanteAux.setEstado(true);
            this.create(estadoRepresentanteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el Estado del Representante");
        }
    }

    @Override
    public void edit(Long idEstadoRepresentante, String nombre) throws Exception {
        try {
            EstadoRepresentante estadoRepresentanteAux = this.find(idEstadoRepresentante);
            estadoRepresentanteAux.setNombre(nombre.toUpperCase());
            this.edit(estadoRepresentanteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el Estado del Representante");
        }
    }

    @Override
    public void remove(Long idEstadoRepresentante) throws Exception {
        try {
            EstadoRepresentante estadoRepresentanteAux = this.find(idEstadoRepresentante);
            estadoRepresentanteAux.setEstado(false);
            this.edit(estadoRepresentanteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el Estado del Representante");
        }
    }

}
