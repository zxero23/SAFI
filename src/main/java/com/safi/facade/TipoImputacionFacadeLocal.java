/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoImputacion;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface TipoImputacionFacadeLocal {

    public void create(TipoImputacion tipoImputacion);

    public void edit(TipoImputacion tipoImputacion);

    public void remove(TipoImputacion tipoImputacion);

    public TipoImputacion find(Object id);

    public List<TipoImputacion> findAll();

    public List<TipoImputacion> findRange(int[] range);

    public int count();

    public List<TipoImputacion> findAll(boolean estado);

    public List<TipoImputacion> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoImputacion, String nombre) throws Exception;

    public void remove(Long idTipoImputacion) throws Exception;

    public List<TipoImputacion> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);    
    
}
