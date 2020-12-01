/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
@Table(name = "recursos_propios")
public class RecursoPropio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "fecha_alta")
    private Date fechaAlta;
    @Column(name = "importe_original", precision = 14, scale = 2)
    private BigDecimal importeOriginal;
    @Column(name = "importe_actual", precision = 14, scale = 2)
    private BigDecimal importeActual;
    private String descripcion;
    @ManyToOne
    private Servicio servicio;
    @OneToMany(mappedBy = "recursoPropio")
    private List<Movimiento> lstMovimientos;
    @OneToOne
    private SaldoAcumulado saldoAcumulado;
    @OneToMany(mappedBy = "recursoPropio")
    private List<RecursoModificado> lstRecursoModificado;
    @OneToOne
    private TipoMoneda tipoMoneda;
    @OneToOne
    private ClasificadorRecursoPropio clasificadorRecurso;
    private boolean estado;//borrado lógico
    @ManyToOne
    private Organismo organismo;
    @ManyToOne
    private Ejercicio ejercicio;
    //caracter para migración
    @OneToOne
    private Caracter caracter;
    //concepto para migración 18/02/2020 puede ser XX ,01,02,etc.
    private String concepto;

    public RecursoPropio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Caracter getCaracter() {
        return caracter;
    }

    public void setCaracter(Caracter caracter) {
        this.caracter = caracter;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public BigDecimal getImporteOriginal() {
        return importeOriginal;
    }

    public void setImporteOriginal(BigDecimal importeOriginal) {
        this.importeOriginal = importeOriginal;
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

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public List<Movimiento> getLstMovimientos() {
        return lstMovimientos.stream()
                .sorted((p1, p2) -> p2.getFechaAlta().compareTo(p1.getFechaAlta()))
                .collect(Collectors.toList());
               
    }

    public void setLstMovimientos(List<Movimiento> lstMovimientos) {
        this.lstMovimientos = lstMovimientos;
    }

    public SaldoAcumulado getSaldoAcumulado() {
        return saldoAcumulado;
    }

    public void setSaldoAcumulado(SaldoAcumulado saldoAcumulado) {
        this.saldoAcumulado = saldoAcumulado;
    }

    public List<RecursoModificado> getLstRecursoModificado() {
        return lstRecursoModificado;
    }

    public List<RecursoModificado> getLstRecursoModificadoOrderByNumAsc() {
        return lstRecursoModificado.stream()
                .sorted((p1, p2) -> p1.getNumeroModificacion().compareTo(p2.getNumeroModificacion()))
                .collect(Collectors.toList());
    }

    public void setLstRecursoModificado(List<RecursoModificado> lstRecursoModificado) {
        this.lstRecursoModificado = lstRecursoModificado;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public ClasificadorRecursoPropio getClasificadorRecurso() {
        return clasificadorRecurso;
    }

    public void setClasificadorRecurso(ClasificadorRecursoPropio clasificadorRecurso) {
        this.clasificadorRecurso = clasificadorRecurso;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    public double getImporteModificaciones() {
        double retorno = 0.0;
        for (RecursoModificado rm : this.getLstRecursoModificado()) {
            if (rm.getImporte() != null) {
                retorno = retorno + rm.getImporte().doubleValue();
            }
        }
        return retorno;
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
        if (!(object instanceof RecursoPropio)) {
            return false;
        }
        RecursoPropio other = (RecursoPropio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.safi.entity.RecursoPropio[ id=" + id + " ]";
    }

}
