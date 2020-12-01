/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Funcion;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface FuncionFacadeLocal {

    public void create(Funcion funcion);

    public void edit(Funcion funcion);

    public void remove(Funcion funcion);

    public Funcion find(Object id);
    
    public Funcion find(Long idTipoOrdenGasto, Long idTipoFondo, 
            Long idTipoImputacion, Long idTipoAcumulador, int tipoOperacion);    

    public List<Funcion> findRange(int[] range);

    public int count();

    public List<Funcion> findAll(boolean estado);
    
     public List<Funcion> findAll(Long idTipoOrdenGasto, Long idTipoFondo, 
             Long idTipoImputacion, Long idTipoAcumulador);

    public void create(Long idTipoOrdenGasto, Long idTipoFondo, 
            Long idTipoImputacion, Long idTipoAcumulador, int tipoOperacion) throws Exception;

    public void edit(Long idFuncion, Long idTipoOrdenGasto, Long idTipoFondo,
            Long idTipoImputacion, Long idTipoAcumulador, int tipoOperacion) throws Exception;

    public void remove(Long idFuncion) throws Exception;   

    public List<Funcion> findAll(Long idTipoOrdenGasto, Long idTipoFondo, Long idTipoImputacion, Long idTipoAcumulador, int first, int pageSize);

    public Long countAll(Long idTipoOrdenGasto, Long idTipoFondo, Long idTipoImputacion, Long idTipoAcumulador);
    
}
