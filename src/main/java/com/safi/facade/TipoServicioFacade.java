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
import com.safi.entity.TipoServicio;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class TipoServicioFacade extends AbstractFacade<TipoServicio> implements TipoServicioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoServicioFacade() {
        super(TipoServicio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoServicio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoServicio as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoServicio> findAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoServicio as o WHERE o.estado = true ");
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
    public List<TipoServicio> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoServicio as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoServicio as o WHERE o.estado = true ");
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
            TipoServicio tipoServicioAux = new TipoServicio();
            tipoServicioAux.setNombre(nombre.toUpperCase());
            tipoServicioAux.setEstado(true);
            this.create(tipoServicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el tipo de servicio");
        }
    }

    @Override
    public void edit(Long idTipoServicio, String nombre) throws Exception {
        try {
            TipoServicio tipoServicioAux = this.find(idTipoServicio);
            tipoServicioAux.setNombre(nombre.toUpperCase());
            this.edit(tipoServicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el tipo de servicio");
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoServicio> findByName(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoServicio as o WHERE o.estado = true");
        if (!(nombre.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '").append(Utilidad.desacentuar(nombre).toUpperCase()).append("'");
        }
        query.append(" ORDER BY o.nombre ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @Override
    public void remove(Long idTipoServicio) throws Exception {
        try {
            TipoServicio tipoServicioAux = this.find(idTipoServicio);
            tipoServicioAux.setEstado(false);
            this.edit(tipoServicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el tipo de servicio");
        }
    }

}
