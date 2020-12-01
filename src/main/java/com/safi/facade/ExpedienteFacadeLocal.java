/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Expediente;
import com.safi.entity.Movimiento;
import com.safi.utilidad.ExpedienteAux;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface ExpedienteFacadeLocal {

    public void create(Expediente expediente);

    public void edit(Expediente expediente);

    public void remove(Expediente expediente);

    public Expediente find(Object id);

    public List<Expediente> findAll();

    public List<Expediente> findRange(int[] range);

    public int count();
    
    public List<Expediente> findAll(boolean estado) ;
     
    public List<Expediente> findAll(Long numero);

    public Expediente findPorNroExpediente(Long codigo, Long nroExpediente, String anio);
    
    public List<Movimiento> findAll(Long idEjercicio, Long idServicio); 
    
    public List<Movimiento> findAll(Long organismoExpediente,String numeroExpediente,String anio,Long idEjercicio,Long idCuentaBancaria);
     
    public List<ExpedienteAux> findExpedientes(Long idEjercicio, String numeroExpediente, Long idServicio, Long idCuenta, Long organismoExpediente,String anio);
    
}
