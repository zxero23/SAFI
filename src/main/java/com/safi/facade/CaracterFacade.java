/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.safi.entity.Caracter;

/**
 *
 * @author ccpm
 */
@Stateless
public class CaracterFacade extends AbstractFacade<Caracter> implements CaracterFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CaracterFacade() {
        super(Caracter.class);
    }
    
}
