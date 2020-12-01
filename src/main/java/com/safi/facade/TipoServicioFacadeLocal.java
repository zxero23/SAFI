/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoServicio;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface TipoServicioFacadeLocal {

    void create(TipoServicio tipoServicio);

    void edit(TipoServicio tipoServicio);

    void remove(TipoServicio tipoServicio);

    TipoServicio find(Object id);

    List<TipoServicio> findAll();

    List<TipoServicio> findRange(int[] range);

    int count();

    public List<TipoServicio> findAll(boolean estado);

    public List<TipoServicio> findAll(String nombre);

    public List<TipoServicio> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoServicio, String nombre) throws Exception;

    public List<TipoServicio> findByName(String nombre);

    public void remove(Long idTipoServicio) throws Exception;
    
}
