/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Provincia;

/**
 *
 * @author Alvarenga Angel
 */
@Local
public interface ProvinciaFacadeLocal {

    public void create(Provincia provincia);

    public void edit(Provincia provincia);

    public void remove(Provincia provincia);

    public Provincia find(Object id);

    public List<Provincia> findAll();

    public List<Provincia> findRange(int[] range);

    public int count();

    public List<Provincia> findAll(boolean estado);

    public List<Provincia> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void remove(Long idProvincia) throws Exception;

    public void edit(Long idProvincia, String nombre) throws Exception;

    public List<Provincia> findAll( String nombre, int first, int pageSize);

    public Long countAll( String nombre);    
    
}
