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
import com.safi.entity.EstadoEjercicio;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class EstadoEjercicioFacade extends AbstractFacade<EstadoEjercicio> implements EstadoEjercicioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoEjercicioFacade() {
        super(EstadoEjercicio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<EstadoEjercicio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from EstadoEjercicio as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<EstadoEjercicio> findAll(String nombre) {
        return em.createQuery("select object(o) FROM EstadoEjercicio as o WHERE o.estado = true AND o.nombre LIKE '%" + nombre + "%' ORDER BY o.id ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<EstadoEjercicio> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM EstadoEjercicio as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM EstadoEjercicio as o WHERE o.estado = true ");
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
            EstadoEjercicio estadoCuentaBancariaAux = new EstadoEjercicio();
            estadoCuentaBancariaAux.setNombre(nombre.toUpperCase());
            estadoCuentaBancariaAux.setEstado(true);
            this.create(estadoCuentaBancariaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el Estado de la Cuenta Bancaria");
        }
    }

    @Override
    public void edit(Long idEstadoEjercicio, String nombre) throws Exception {
        try {
            EstadoEjercicio estadoCuentaBancariaAux = this.find(idEstadoEjercicio);
            estadoCuentaBancariaAux.setNombre(nombre.toUpperCase());
            this.edit(estadoCuentaBancariaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el Estado de la Cuenta Bancaria");
        }
    }

    @Override
    public void remove(Long idEstadoEjercicio) throws Exception {
        try {
            EstadoEjercicio estadoCuentaBancariaAux = this.find(idEstadoEjercicio);
            estadoCuentaBancariaAux.setEstado(false);
            this.edit(estadoCuentaBancariaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el Estado de la Cuenta Bancaria");
        }
    }

}
