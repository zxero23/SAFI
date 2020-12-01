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
import com.safi.entity.ActionItem;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class ActionItemFacade extends AbstractFacade<ActionItem> implements ActionItemFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActionItemFacade() {
        super(ActionItem.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ActionItem> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from ActionItem as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ActionItem> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM ActionItem as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
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
        query.append("select COUNT(o) FROM ActionItem as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ActionItem> findAll(List<Long> actionsId) {
        String iDes = actionsId.toString().substring(1, actionsId.toString().length() - 1);
        return em.createQuery("select object (act) from ActionItem as act where act.id in (" + iDes + ")").getResultList();
    }

    @Override
    public void create(String nombre) throws Exception {
        try {
            ActionItem actionItems = new ActionItem();
            actionItems.setNombre(nombre);
            actionItems.setEstado(true);
            this.create(actionItems);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la acción.");
        }
    }

    @Override
    public void remove(Long idAccion) throws Exception {
        try {
            ActionItem actionItems = this.find(idAccion);
            actionItems.setEstado(false);
            this.edit(actionItems);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la acción.");
        }
    }

    @Override
    public void edit(Long idAccion, String nombreAccion) throws Exception {
        try {
            ActionItem actionItems = this.find(idAccion);
            actionItems.setNombre(nombreAccion);
            this.edit(actionItems);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la acción.");
        }
    }

}
