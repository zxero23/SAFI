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
import com.safi.entity.TipoCuenta;
import com.safi.facade.AbstractFacade;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class TipoCuentaFacade extends AbstractFacade<TipoCuenta> implements TipoCuentaFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoCuentaFacade() {
        super(TipoCuenta.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoCuenta> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoCuenta as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoCuenta> findAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoCuenta as o WHERE o.estado = true ");
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
    public List<TipoCuenta> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoCuenta as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoCuenta as o WHERE o.estado = true ");
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
            TipoCuenta tipoMonedaAux = new TipoCuenta();
            tipoMonedaAux.setNombre(nombre.toUpperCase());
            tipoMonedaAux.setEstado(true);
            this.create(tipoMonedaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el tipo de cuenta");
        }
    }

    @Override
    public void edit(Long idTipoCuenta, String nombre) throws Exception {
        try {
            TipoCuenta tipoMonedaAux = this.find(idTipoCuenta);
            tipoMonedaAux.setNombre(nombre.toUpperCase());
            this.edit(tipoMonedaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el tipo de cuenta");
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoCuenta> findByName(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoCuenta as o WHERE o.estado = true");
        if (!(nombre.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '").append(Utilidad.desacentuar(nombre).toUpperCase()).append("'");
        }
        query.append(" ORDER BY o.nombre ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @Override
    public void remove(Long idTipoCuenta) throws Exception {
        try {
            TipoCuenta tipoMonedaAux = this.find(idTipoCuenta);
            tipoMonedaAux.setEstado(false);
            this.edit(tipoMonedaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el tipo de cuenta");
        }
    }
}
