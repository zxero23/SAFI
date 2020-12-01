/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.safi.entity.InstrumentoLegal;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class InstrumentoLegalFacade extends AbstractFacade<InstrumentoLegal> implements InstrumentoLegalFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstrumentoLegalFacade() {
        super(InstrumentoLegal.class);
    }
    
}
