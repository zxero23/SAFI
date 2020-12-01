/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Funcion;
import com.safi.entity.Movimiento;
import com.safi.entity.SaldoAcumulado;
import com.safi.utilidad.Utilidad;


/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class SaldoAcumuladoFacade extends AbstractFacade<SaldoAcumulado> implements SaldoAcumuladoFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    private TipoAcumuladorFacadeLocal tipoAcumuladorFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaldoAcumuladoFacade() {
        super(SaldoAcumulado.class);
    }
    
    /**
     * @author Alvarenga Angel
     * @param cuenta
     * @param funciones
     * @param importe
     * @param movimiento
     * @throws Exception 
     */
    @Override
    public void actualizarSaldos(CuentaBancaria cuenta, List<Funcion> funciones, BigDecimal importe,
            Movimiento movimiento) throws Exception {
        try {
            
            for (Funcion auxF : funciones) {
                SaldoAcumulado saldoAcumulado = this.find(auxF.getTipoAcumulador().getId(), cuenta);
                if (saldoAcumulado == null) {
                    this.create(cuenta);
                    saldoAcumulado = this.find(auxF.getTipoAcumulador().getId(), cuenta);
                    
                }
                saldoAcumulado.setSaldoAnterior(saldoAcumulado.getSaldoActual());
                switch (auxF.getTipoOperacion()) {
                    case 1:
                        saldoAcumulado.setSaldoActual(saldoAcumulado.getSaldoActual().add(importe));
                        break;
                    case -1:
                        saldoAcumulado.setSaldoActual(saldoAcumulado.getSaldoActual().subtract(importe));
                        break;
                }
                if (saldoAcumulado.getTipoAcumulador().getId().toString().equals("16")) {
                    cuenta.setSaldo(saldoAcumulado.getSaldoActual());
                }
                saldoAcumulado.setFechaActual(new Date());
                saldoAcumulado.setCuentaBancaria(cuenta);
                this.edit(saldoAcumulado);
            }
        } catch (Exception e) {
            throw new Exception("Error al actualizar los saldos de la cuenta." + e.getMessage());
        }
    }
    
    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @param cuenta 
     */
    @Override
    public void create(CuentaBancaria cuenta) {
        Long id = 0L;
        for (int i = 0; i < 17; i++) {
            id++;
            SaldoAcumulado saldo = new SaldoAcumulado();
            saldo.setTipoAcumulador(tipoAcumuladorFacade.find(id));
            if (saldo.getTipoAcumulador().getId().toString().equals("16")) {
                saldo.setSaldoActual(cuenta.getSaldo());
            } else {
                saldo.setSaldoActual(BigDecimal.ZERO);
            }
            saldo.setSaldoAnterior(BigDecimal.ZERO);
            saldo.setSaldoOriginal(BigDecimal.ZERO);
            saldo.setFechaActual(new Date());
            saldo.setEstado(true);
            cuenta.getLstSaldoAcumulado().add(saldo);
        }
        this.cuentaBancariaFacade.edit(cuenta);
    }
    
    /**
     * @author Alvarenga Angel
     * @param acumulador
     * @param cuenta
     * @return 
     */
    private SaldoAcumulado find(Long idAcumulador, CuentaBancaria cuenta) {
        try{
        SaldoAcumulado saldo = null;
        for (SaldoAcumulado aux : cuenta.getLstSaldoAcumulado()) {
            if (aux.getTipoAcumulador().getId().equals(idAcumulador)) {
                saldo = aux;
                break;
            }
        }
        return saldo;
        }catch(Exception ex){            
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    
    /**
     * @author González Facundo
     * @param cuenta     
     * @return 
     */
    @Override
    public List<SaldoAcumulado> find( CuentaBancaria cuenta) {
        List<SaldoAcumulado> saldos = new ArrayList<>();
            
        for (SaldoAcumulado aux : cuenta.getLstSaldoAcumulado()) {            
                saldos.add(aux);                
            }  
        return saldos.stream()               
                .sorted((s1,s2)->(s2.getFechaActual().compareTo(s1.getFechaActual())))//ordena por fecha 
                .sorted((s1,s2)->(s1.getTipoAcumulador().getId().compareTo(s2.getTipoAcumulador().getId())))//ordena por tipo acumulador    
                .filter( Utilidad.distinctByKey(p -> p.getTipoAcumulador()) )   //se distingue entre id de acumulador               
                .collect(Collectors.toList());
                 
    }
    
    
    
    @Override
    public void actualizarSaldoCuentaBancaria(CuentaBancaria cuenta,BigDecimal importe,List<Funcion> lstFuncion){
        
        BigDecimal importeAcumulador=BigDecimal.ZERO;
        BigDecimal saldoNuevo=BigDecimal.ZERO;
        for (Funcion funcion:lstFuncion){
            if (funcion.getTipoAcumulador().getId().equals(16L)){
                importeAcumulador= importe.multiply(BigDecimal.valueOf(funcion.getTipoOperacion()));
                saldoNuevo=cuenta.getSaldo().add(importeAcumulador);               
                cuenta.setSaldo(saldoNuevo);
                this.cuentaBancariaFacade.edit(cuenta);
                
                break;
            }
        }
        
            
        
        
        
        
        
        
    }
   
    
    
    
}
