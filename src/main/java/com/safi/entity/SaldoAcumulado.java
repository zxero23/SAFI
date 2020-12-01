/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name="saldos_acumulados")
public class SaldoAcumulado implements Serializable {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fechaActual")
    private Date fechaActual;
    @Column (name="saldo_original", precision=14, scale=2)
    private BigDecimal saldoOriginal;
    @Column(name="saldo_anterior", precision = 14, scale = 2)
    private BigDecimal saldoAnterior;
    @Column(name="saldo_actual", precision = 14, scale = 2)
    private BigDecimal saldoActual;
    @OneToOne
    private TipoAcumulador tipoAcumulador;
    private boolean estado;//borrado lógico
    @ManyToOne
    private CuentaBancaria cuentaBancaria;

    public SaldoAcumulado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public BigDecimal getSaldoOriginal() {
        return saldoOriginal;
    }

    public void setSaldoOriginal(BigDecimal saldoOriginal) {
        this.saldoOriginal = saldoOriginal;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public TipoAcumulador getTipoAcumulador() {
        return tipoAcumulador;
    }

    public void setTipoAcumulador(TipoAcumulador tipoAcumulador) {
        this.tipoAcumulador = tipoAcumulador;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldoAcumulado)) {
            return false;
        }
        SaldoAcumulado other = (SaldoAcumulado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.safi.entity.SaldoActual[ id=" + id + " ]";
    }
    
}
