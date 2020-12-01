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
import com.safi.entity.TipoClasificadorRecursoPropio;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class TipoClasificadorRecursoPropioFacade extends AbstractFacade<TipoClasificadorRecursoPropio> implements TipoClasificadorRecursoPropioFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoClasificadorRecursoPropioFacade() {
        super(TipoClasificadorRecursoPropio.class);
    }
    
    /**
     * @param estado
     * @author Alvarenga Angel
     * @return List TipoClasificadorRecursoPropio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoClasificadorRecursoPropio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoClasificadorRecursoPropio as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoClasificadorRecursoPropio> findAll(String nombre) {
        Query consulta = em.createQuery("select object(o) from TipoClasificadorRecursoPropio as o WHERE o.estado = :p1 AND upper(FUNCTION('unaccent', o.nombre)) like '%".concat(Utilidad.desacentuar(nombre)).concat("%'"));
        consulta.setParameter("p1", true);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<TipoClasificadorRecursoPropio> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM TipoClasificadorRecursoPropio as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM TipoClasificadorRecursoPropio as o WHERE o.estado = true ");
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
            TipoClasificadorRecursoPropio tipoClasificadorAux = new TipoClasificadorRecursoPropio();
            tipoClasificadorAux.setNombre(nombre.toUpperCase());            
            tipoClasificadorAux.setEstado(true);
            this.create(tipoClasificadorAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el tipo de clasificador de recursos propios.");
        }
    }
    
    @Override
    public void edit(Long idTipoClasificadorRecursoPropio, String nombre) throws Exception {
        try {
            TipoClasificadorRecursoPropio tipoClasificadorRecursoPropioAux = this.find(idTipoClasificadorRecursoPropio);
            tipoClasificadorRecursoPropioAux.setNombre(nombre.toUpperCase());            
            this.edit(tipoClasificadorRecursoPropioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el tipo de clasificador de recursos propios.");
        }
    }
    
    @Override
    public void remove(Long idTipoClasificadorRecursoPropio) throws Exception {
        try {
            TipoClasificadorRecursoPropio tipoClasificadorRecursoPropioAux = this.find(idTipoClasificadorRecursoPropio);
            tipoClasificadorRecursoPropioAux.setEstado(false);
            this.edit(tipoClasificadorRecursoPropioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el tipo de clasificador de recursos propios.");
        }
    }
}
