/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Rol;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface RolFacadeLocal {

    void create(Rol rol);

    void edit(Rol rol);

    void remove(Rol rol);

    Rol find(Object id);

    List<Rol> findAll();

    List<Rol> findRange(int[] range);

    int count();

    public List<Rol> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public List<Rol> findAll(boolean estado);

    public void edit(Long idRol, String nombre) throws Exception;

    public void remove(Long idRol) throws Exception;

    public List<Rol> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
