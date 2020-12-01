/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoOrdenGasto;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface TipoOrdenGastoFacadeLocal {

    public void create(TipoOrdenGasto tipoOrdenGasto);

    public void edit(TipoOrdenGasto tipoOrdenGasto);

    public void remove(TipoOrdenGasto tipoOrdenGasto);

    public TipoOrdenGasto find(Object id);

    public List<TipoOrdenGasto> findAll();

    public List<TipoOrdenGasto> findRange(int[] range);

    public int count();

    public List<TipoOrdenGasto> findAll(boolean estado);

    public List<TipoOrdenGasto> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoOrdenGasto, String nombre) throws Exception;

    public void remove(Long idTipoOrdenGasto) throws Exception;

    public List<TipoOrdenGasto> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);

    
    
}
