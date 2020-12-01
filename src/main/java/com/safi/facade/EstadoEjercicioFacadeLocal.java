/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.EstadoEjercicio;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface EstadoEjercicioFacadeLocal {

    public void create(EstadoEjercicio estadoEjercicio);

    public void edit(EstadoEjercicio estadoEjercicio);

    public void remove(EstadoEjercicio estadoEjercicio);

    public EstadoEjercicio find(Object id);

    public List<EstadoEjercicio> findAll();

    public List<EstadoEjercicio> findRange(int[] range);

    public int count();

    public List<EstadoEjercicio> findAll(boolean estado);

    public List<EstadoEjercicio> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idEstadoEjercicio, String nombre) throws Exception;

    public void remove(Long idEstadoEjercicio) throws Exception;

    public List<EstadoEjercicio> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
