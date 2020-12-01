/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.EstadoRepresentante;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface EstadoRepresentanteFacadeLocal {

    void create(EstadoRepresentante estadoRepresentante);

    void edit(EstadoRepresentante estadoRepresentante);

    void remove(EstadoRepresentante estadoRepresentante);

    EstadoRepresentante find(Object id);

    List<EstadoRepresentante> findAll();

    List<EstadoRepresentante> findRange(int[] range);

    int count();

    public List<EstadoRepresentante> findAll(boolean estado);

    public List<EstadoRepresentante> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idEstadoRepresentante, String nombre) throws Exception;

    public void remove(Long idEstadoRepresentante) throws Exception;

    public List<EstadoRepresentante> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
