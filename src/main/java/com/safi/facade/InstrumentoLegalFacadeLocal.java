/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.InstrumentoLegal;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface InstrumentoLegalFacadeLocal {

    public void create(InstrumentoLegal instrumentoLegal);

    public void edit(InstrumentoLegal instrumentoLegal);

    public void remove(InstrumentoLegal instrumentoLegal);

    public InstrumentoLegal find(Object id);

    public List<InstrumentoLegal> findAll();

    public List<InstrumentoLegal> findRange(int[] range);

    public int count();
    
}
