/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name="funciones")
public class Funcion implements Serializable {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private TipoOrdenGasto tipoOrdenGasto;
    @OneToOne
    private TipoFondo tipoFondo;
    @OneToOne
    private TipoImputacion tipoImputacion;
    @OneToOne
    private TipoAcumulador tipoAcumulador;
    @Column(name="tipo_operacion")
    private int tipoOperacion;//valores 1 y -1 positivo suma negativo resta
    private boolean estado;//borrado lógico
    
    
    public Funcion() {
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoOrdenGasto getTipoOrdenGasto() {
        return tipoOrdenGasto;
    }

    public void setTipoOrdenGasto(TipoOrdenGasto tipoOrdenGasto) {
        this.tipoOrdenGasto = tipoOrdenGasto;
    }

    public TipoFondo getTipoFondo() {
        return tipoFondo;
    }

    public void setTipoFondo(TipoFondo tipoFondo) {
        this.tipoFondo = tipoFondo;
    }

    public TipoImputacion getTipoImputacion() {
        return tipoImputacion;
    }

    public void setTipoImputacion(TipoImputacion tipoImputacion) {
        this.tipoImputacion = tipoImputacion;
    }

    public TipoAcumulador getTipoAcumulador() {
        return tipoAcumulador;
    }

    public void setTipoAcumulador(TipoAcumulador tipoAcumulador) {
        this.tipoAcumulador = tipoAcumulador;
    }

    public int getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(int tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof Funcion)) {
            return false;
        }
        Funcion other = (Funcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.cg.entity.Funcion[ id=" + id + " ]";
    }
    
}
