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
@Table(name="recursos_modificados")
public class RecursoModificado implements Serializable {    
    @ManyToOne
    private RecursoPropio recursoPropio;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fecha_alta")
    private Date fechaAlta;    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fecha_modificacion")
    private Date fechaMoficacion;
    @Column(name="importe_actual", precision = 14, scale = 2)
    private BigDecimal importeActual;
    private String descripcion;
    private Integer numeroModificacion;
    @Column(name="importe", precision = 14, scale = 2)
    private BigDecimal importe;
    @OneToOne
    private InstrumentoLegal instrumentoLegal;
    @OneToOne
    private TipoMoneda tipoMoneda;
    private boolean estado;//borrado lógico

    public RecursoPropio getRecursoPropio() {
        return recursoPropio;
    }

    public void setRecursoPropio(RecursoPropio recursoPropio) {
        this.recursoPropio = recursoPropio;
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

    public Date getFechaMoficacion() {
        return fechaMoficacion;
    }

    public void setFechaMoficacion(Date fechaMoficacion) {
        this.fechaMoficacion = fechaMoficacion;
    }

    public BigDecimal getImporteActual() {
        return importeActual;
    }

    public void setImporteActual(BigDecimal importeActual) {
        this.importeActual = importeActual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getNumeroModificacion() {
        return numeroModificacion;
    }

    public void setNumeroModificacion(Integer numeroModificacion) {
        this.numeroModificacion = numeroModificacion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public InstrumentoLegal getInstrumentoLegal() {
        return instrumentoLegal;
    }

    public void setInstrumentoLegal(InstrumentoLegal instrumentoLegal) {
        this.instrumentoLegal = instrumentoLegal;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
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
        if (!(object instanceof RecursoModificado)) {
            return false;
        }
        RecursoModificado other = (RecursoModificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.safi.entity.RecursoModificado[ id=" + id + " ]";
    }
    
}
