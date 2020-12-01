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
import com.safi.entity.Provincia;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel
 */
@Stateless
public class ProvinciaFacade extends AbstractFacade<Provincia> implements ProvinciaFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProvinciaFacade() {
        super(Provincia.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Provincia> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Provincia as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Provincia> findAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Provincia as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        query.append("ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Provincia> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Provincia as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        query.append(" ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Provincia as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public void create(String nombre) throws Exception {
        try {
            boolean isNameEqual = false;
            List<Provincia> listProvincia = this.findAll(true);
            for (Provincia provincia : listProvincia) {
                if (provincia.getNombre().equalsIgnoreCase(nombre)) {
                    isNameEqual = true;
                    break;
                }
            }
            if (isNameEqual) {
                throw new Exception("El nombre de la provincia ya existe");
            }
            Provincia provinciaAux = new Provincia();
            provinciaAux.setNombre(nombre.toUpperCase());
            provinciaAux.setEstado(true);
            this.create(provinciaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la provincia: " + e.getMessage());
        }
    }

    @Override
    public void remove(Long idProvincia) throws Exception {
        try {
            Provincia provinciaAux = this.find(idProvincia);
            provinciaAux.setEstado(false);
            this.edit(provinciaAux);
        } catch (Exception e) {
            throw new Exception("Error al intetar borrar la provincia");
        }
    }

    @Override
    public void edit(Long idProvincia, String nombre) throws Exception {
        try {
            Provincia provinciaAux = this.find(idProvincia);
            provinciaAux.setNombre(nombre.toUpperCase());
            this.edit(provinciaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la provincia");
        }
    }

}
