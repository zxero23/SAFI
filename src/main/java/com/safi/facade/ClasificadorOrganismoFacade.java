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
import com.safi.entity.ClasificadorOrganismo;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class ClasificadorOrganismoFacade extends AbstractFacade<ClasificadorOrganismo> implements ClasificadorOrganismoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClasificadorOrganismoFacade() {
        super(ClasificadorOrganismo.class);
    }

    /**
     * @author Alvarenga Angel
     * @param estado
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorOrganismo> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from ClasificadorOrganismo as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    /**
     * @author Alvarenga Angel
     * @param nombre
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorOrganismo> findAll(String nombre) {
        return em.createQuery("select object(o) FROM ClasificadorOrganismo as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombre) + "%' order by o.nombre").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorOrganismo> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM ClasificadorOrganismo as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
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
        query.append("select COUNT(o) FROM ClasificadorOrganismo as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    /**
     * @author Alvarenga Angel
     * @param nombre
     * @throws Exception
     */
    @Override
    public void create(String nombre) throws Exception {
        try {
            ClasificadorOrganismo clasificadorOrganismoAux = new ClasificadorOrganismo();
            clasificadorOrganismoAux.setNombre(nombre.toUpperCase());
            clasificadorOrganismoAux.setEstado(true);
            this.create(clasificadorOrganismoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el clasificador de organismos.");
        }
    }

    /**
     * @author Alvarenga Angel
     * @param idClasificadorOrganismo
     * @param nombre
     * @throws Exception
     */
    @Override
    public void edit(Long idClasificadorOrganismo, String nombre) throws Exception {
        try {
            ClasificadorOrganismo clasificadorOrganismoAux = this.find(idClasificadorOrganismo);
            clasificadorOrganismoAux.setNombre(nombre.toUpperCase());
            this.edit(clasificadorOrganismoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el clasificador de organismos.");
        }
    }

    /**
     * @author Alvarenga Angel
     * @param idClasificadorOrganismo
     * @throws Exception
     */
    @Override
    public void remove(Long idClasificadorOrganismo) throws Exception {
        try {
            ClasificadorOrganismo clasificadorOrganismoAux = this.find(idClasificadorOrganismo);
            clasificadorOrganismoAux.setEstado(false);
            this.edit(clasificadorOrganismoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el clasificador de organismos.");
        }
    }

}
