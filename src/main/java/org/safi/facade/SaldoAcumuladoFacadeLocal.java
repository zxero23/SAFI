/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safi.facade;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import org.safi.entity.CuentaBancaria;
import org.safi.entity.Funcion;
import org.safi.entity.Movimiento;
import org.safi.entity.SaldoAcumulado;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface SaldoAcumuladoFacadeLocal {

    void create(SaldoAcumulado saldoAcumulado);

    void edit(SaldoAcumulado saldoAcumulado);

    void remove(SaldoAcumulado saldoAcumulado);

    SaldoAcumulado find(Object id);

    List<SaldoAcumulado> findAll();

    List<SaldoAcumulado> findRange(int[] range);

    int count();

    public void actualizarSaldos(CuentaBancaria cuenta, List<Funcion> funciones, 
            BigDecimal importe, Movimiento movimiento) throws Exception;
    
    public void create(CuentaBancaria cuenta) ;
    
    public List<SaldoAcumulado> find( CuentaBancaria cuenta);
    
     public void actualizarSaldoCuentaBancaria(CuentaBancaria cuenta,BigDecimal importe,List<Funcion> lstFuncion);
    
}
