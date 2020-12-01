/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoCuenta;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface TipoCuentaFacadeLocal {

    void create(TipoCuenta tipoCuenta);

    void edit(TipoCuenta tipoCuenta);

    void remove(TipoCuenta tipoCuenta);

    TipoCuenta find(Object id);

    List<TipoCuenta> findAll();

    List<TipoCuenta> findRange(int[] range);

    int count();

    public List<TipoCuenta> findAll(boolean estado);

    public List<TipoCuenta> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoCuenta, String nombre) throws Exception;

    public void remove(Long idTipoCuenta) throws Exception;

    public List<TipoCuenta> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);

    public List<TipoCuenta> findByName(String nombre);
    
}
