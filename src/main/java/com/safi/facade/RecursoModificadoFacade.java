/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.RecursoModificado;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class RecursoModificadoFacade extends AbstractFacade<RecursoModificado> implements RecursoModificadoFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecursoModificadoFacade() {
        super(RecursoModificado.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Integer maximoNroModificacion(Long idRecursoPropio) {
        try{
            
        Query consulta = em.createQuery("select MAX(o.numeroModificacion) from RecursoModificado as o WHERE o.recursoPropio.id=:p1");  
        consulta.setParameter("p1", idRecursoPropio);
        return (Integer)consulta.getSingleResult();
        }catch(Exception ex){
            return 0;
        }
    }
    
    
}
