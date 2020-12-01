/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.RecursoModificado;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface RecursoModificadoFacadeLocal {

    public void create(RecursoModificado recursoModificado);

    public void edit(RecursoModificado recursoModificado);

    public void remove(RecursoModificado recursoModificado);

    public RecursoModificado find(Object id);

    public List<RecursoModificado> findAll();

    public List<RecursoModificado> findRange(int[] range);

    public int count();
    
    public Integer maximoNroModificacion(Long idRecursoPropio) ;
}
