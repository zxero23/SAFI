/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoMoneda;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@Local
public interface TipoMonedaFacadeLocal {

    public void create(TipoMoneda tipoMoneda);

    public void edit(TipoMoneda tipoMoneda);

    public void remove(TipoMoneda tipoMoneda);

    public TipoMoneda find(Object id);

    public List<TipoMoneda> findAll();

    public List<TipoMoneda> findRange(int[] range);

    public int count();

    public List<TipoMoneda> findAll(boolean estado);

    public List<TipoMoneda> findAll(String nombre);

    public void create(String nombre, String descripcion) throws Exception;

    public void edit(Long idTipoMoneda, String nombre, String descripcion) throws Exception;

    public void remove(Long idTipoMoneda) throws Exception;

    public List<TipoMoneda> findByName(String nombre);

    public List<TipoMoneda> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
