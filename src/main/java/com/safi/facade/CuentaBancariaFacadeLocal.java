/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Representante;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface CuentaBancariaFacadeLocal {

    public void create(CuentaBancaria cuentaBancaria);

    public void edit(CuentaBancaria cuentaBancaria);

    public void remove(CuentaBancaria cuentaBancaria);

    public CuentaBancaria find(Object id);

    public List<CuentaBancaria> findAll();

    public List<CuentaBancaria> findRange(int[] range);

    public int count();

    public List<CuentaBancaria> findAll(boolean estado);
    
    public List<CuentaBancaria> findAll(Long idServicio, Long idBanco, 
            Long idEstado, Long numero, String descripcion);
    
    public List<CuentaBancaria> findAll(Long idEjercicio, Long idServicio, 
            Long idEstadoCuenta);
    
     public List<CuentaBancaria> findAll(Long idEjercicio, Long idServicio);

    public void create(Long idEjercicio,Long idServicio,Long idBanco, Date fechaAlta, 
            Long nroCuenta, String descripcion, String cbu, String alias, 
            Long idTipoMoneda, Long idTipoCuenta,String url) throws Exception;
    
    public void edit(Long idCuentaBancaria,Long idEjercicio, Long numero, String descripcion, 
            String CBU, Date fechaAlta, Date fechaBaja, Long idEstadoCuenta, String alias,
            Long idTipoMoneda, Long idTipoCuenta,String url,Long idBanco) throws Exception;

    public void remove(Long idCuentaBancaria) throws Exception;

    public void edit(Long id, Long idAgente, Long idRol, Date fechaIncio, Date fechaFin) throws Exception;

    public void edit(Long id, List<Long> lstIdRepresentantes) throws Exception;

    public List<CuentaBancaria> findAll(Long idServicio, Long idBanco, Long idEstado,
            Long numero, String descripcion, int saldoCero, Long idEjercicio, int first, int pageSize);

    public Long countAll(Long idServicio, Long idBanco, Long idEstado,
            Long numero, String descripcion, int saldoCero, Long idEjercicio);

    

    

   
    
}
