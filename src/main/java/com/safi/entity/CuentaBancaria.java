
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name="cuentas_bancarias")
public class CuentaBancaria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fecha_alta")  
    private Date fechaAlta;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fecha_baja")
    private Date fechaBaja;    
    private Long numero;
    @Column(name = "alias_cb")
    private String alias;
    private String descripcion;
    private String cbu;
    private String url;
    @Column(nullable = false)
    private BigDecimal saldo;
    @OneToOne
    private EstadoCuentaBancaria estadoCuentaBancaria;    
    @ManyToOne
    private Servicio servicio;
    @OneToOne
    private Banco banco;
    @OneToMany
    private List<Representante> representantes;
    @OneToOne
    private TipoMoneda tipoMoneda;
    @OneToOne
    private TipoCuenta tipoCuenta;
    @OneToOne
    private Ejercicio ejercicio; 
    @OneToMany(mappedBy = "cuentaBancaria")
    private List <Movimiento> lstMovimientos = new ArrayList<>();    
    @OneToMany(mappedBy = "cuentaBancaria")
    private List<SaldoAcumulado> lstSaldoAcumulado = new ArrayList(); //17 saldos para las 17 combinaciones de funciones     
    private boolean estado;//borrado lógico

    public CuentaBancaria() {
    }

    public List<Movimiento> getLstMovimientos() {
        return lstMovimientos;
    }

    public void setLstMovimientos(List<Movimiento> lstMovimientos) {
        this.lstMovimientos = lstMovimientos;
    }

    public List<SaldoAcumulado> getLstSaldoAcumulado() {
        return lstSaldoAcumulado;
    }

    public void setLstSaldoAcumulado(List<SaldoAcumulado> lstSaldoAcumulado) {
        this.lstSaldoAcumulado = lstSaldoAcumulado;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public EstadoCuentaBancaria getEstadoCuentaBancaria() {
        return estadoCuentaBancaria;
    }

    public void setEstadoCuentaBancaria(EstadoCuentaBancaria estadoCuentaBancaria) {
        this.estadoCuentaBancaria = estadoCuentaBancaria;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Representante> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<Representante> representantes) {
        this.representantes = representantes;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentaBancaria)) {
            return false;
        }
        CuentaBancaria other = (CuentaBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.cg.entity.CuentaBancaria[ id=" + id + " ]";
    }
    
}
