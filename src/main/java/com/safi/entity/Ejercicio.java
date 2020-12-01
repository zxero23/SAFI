/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name = "ejercicios")
public class Ejercicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer anio;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_complementaria")
    private Date fechaComplementaria;
    @OneToOne
    private EstadoEjercicio estadoEjercicio;
    @OneToMany
    private List<Periodo> lstPeriodos = new ArrayList<>();
    @OneToMany
    private List<Servicio> lstServicios = new ArrayList<>();
    @OneToMany
    private List<CuentaBancaria> lstCuentasBancarias = new ArrayList<>();
    
    @OneToMany(mappedBy = "ejercicio")
    private List<RecursoPropio> lstRecursoPropio = new ArrayList<>();
    private boolean estado;//borrado lógico
    @OneToOne(mappedBy = "ejercicio")
    private CierreEjercicio cierreEjercicio;
    
    @ManyToMany(mappedBy = "ejercicio", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Servicio> servicio = new HashSet<>();
    

    public Ejercicio() {
    }

    public List<CuentaBancaria> getLstCuentasBancarias() {
        return lstCuentasBancarias;
    }

    public void setLstCuentasBancarias(List<CuentaBancaria> lstCuentasBancarias) {
        this.lstCuentasBancarias = lstCuentasBancarias;
    }

    public List<Servicio> getLstServicios() {
        return lstServicios;
    }
    
    public List<Servicio> getLstServiciosSorted() {
        return lstServicios.stream().filter((servicio) -> (servicio.isEstado()))
                           .sorted((s1, s2) -> (Integer.valueOf(s1.getCodigo()).compareTo(Integer.valueOf(s2.getCodigo()))))
                           .collect(Collectors.toList());
    }

    public void setLstServicios(List<Servicio> lstServicios) {
        this.lstServicios = lstServicios;
    }

    public List<Periodo> getLstPeriodos() {
        return lstPeriodos;
    }

    public List<Periodo> getLstPeriodosOrderByFechaInicio() {
        return lstPeriodos.stream()
                          .sorted(Comparator.comparing(Periodo::getFechaInicio))
                          .collect(Collectors.toList());
    }

    public void setLstPeriodos(List<Periodo> lstPeriodos) {
        this.lstPeriodos = lstPeriodos;
    }

    public Date getFechaComplementaria() {
        return fechaComplementaria;
    }

    public void setFechaComplementaria(Date fechaComplementaria) {
        this.fechaComplementaria = fechaComplementaria;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoEjercicio getEstadoEjercicio() {
        return estadoEjercicio;
    }

    public void setEstadoEjercicio(EstadoEjercicio estadoEjercicio) {
        this.estadoEjercicio = estadoEjercicio;
    }

    public List<RecursoPropio> getLstRecursoPropio() {
        return lstRecursoPropio;
    }

    public void setLstRecursosPropio(List<RecursoPropio> lstRecursoPropio) {
        this.lstRecursoPropio = lstRecursoPropio;
    }

    public CierreEjercicio getCierreEjercicio() {
        return cierreEjercicio;
    }

    public void setCierreEjercicio(CierreEjercicio cierreEjercicio) {
        this.cierreEjercicio = cierreEjercicio;
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
        if (!(object instanceof Ejercicio)) {
            return false;
        }
        Ejercicio other = (Ejercicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.cg.entity.Ejercicio[ id=" + id + " ]";
    }

    public Set<Servicio> getServicio() {
        return servicio;
    }

    public void setServicio(Set<Servicio> servicio) {
        this.servicio = servicio;
    }

    
}
