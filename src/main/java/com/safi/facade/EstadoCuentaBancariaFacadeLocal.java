/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.EstadoCuentaBancaria;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface EstadoCuentaBancariaFacadeLocal {

    public void create(EstadoCuentaBancaria estadoCuentaBancaria);

    public void edit(EstadoCuentaBancaria estadoCuentaBancaria);

    public void remove(EstadoCuentaBancaria estadoCuentaBancaria);

    public EstadoCuentaBancaria find(Object id);

    public List<EstadoCuentaBancaria> findAll();
    
    public List<EstadoCuentaBancaria> findAll(boolean estado);

    public List<EstadoCuentaBancaria> findRange(int[] range);

    public int count();

    public List<EstadoCuentaBancaria> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idEstadoCuentaBancaria, String nombre) throws Exception;

    public void remove(Long idEstadoCuentaBancaria) throws Exception;

    public Long countAll(String nombre);

    public List<EstadoCuentaBancaria> findAll(String nombre, int first, int pageSize);

    public List<EstadoCuentaBancaria> findByName(String nombre);

    
    
}
