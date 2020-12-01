/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoFondo;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface TipoFondoFacadeLocal {

    public void create(TipoFondo tipoFondo);

    public void edit(TipoFondo tipoFondo);

    public void remove(TipoFondo tipoFondo);

    public TipoFondo find(Object id);

    public List<TipoFondo> findAll();

    public List<TipoFondo> findRange(int[] range);

    public int count();

    public List<TipoFondo> findAll(boolean estado);

    public List<TipoFondo> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoFondo, String nombre) throws Exception;

    public void remove(Long idTipoFondo) throws Exception;

    public List<TipoFondo> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);

       
}
