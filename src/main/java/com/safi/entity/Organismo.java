/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Angel Alvarenga
 *
 */
@Entity
@Table(name = "organismos")
public class Organismo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo_organismo")
    private Long codigoOrganismo;
    private String nombre;
    @ManyToMany
    private List<Servicio> servicios = new ArrayList<>();;
    @OneToMany
    private List<Organismo> organismos = new ArrayList<>();;
    @ManyToOne
    private Organismo organismoPadre;   
    private String direccion;
    private String telefonos;
    @Column(name="correo_oficial")
    private String correoOficial;
    @OneToOne
    private ClasificadorOrganismo clasificadorOrganismo;
    @OneToMany(mappedBy = "organismo")
    private List<RecursoPropio> lstRecursoPropio;
    private boolean estado;//borrado lógico
    

    public Organismo() {
        this.organismos = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.lstRecursoPropio = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigoOrganismo() {
        return codigoOrganismo;
    }

    public void setCodigoOrganismo(Long codigoOrganismo) {
        this.codigoOrganismo = codigoOrganismo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }
    
    public List<Servicio> getServiciosSorted() {
        return servicios.stream()
                        .sorted((s1, s2) -> (Integer.valueOf(s1.getCodigo()).compareTo(Integer.valueOf(s2.getCodigo()))))
                        .collect(Collectors.toList());
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Organismo> getOrganismos() {
        return organismos;
    }

    public void setOrganismos(List<Organismo> organismos) {
        this.organismos = organismos;
    }

    public Organismo getOrganismoPadre() {
        return organismoPadre;
    }

    public void setOrganismoPadre(Organismo organismoPadre) {
        this.organismoPadre = organismoPadre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getCorreoOficial() {
        return correoOficial;
    }

    public void setCorreoOficial(String correoOficial) {
        this.correoOficial = correoOficial;
    }

    public ClasificadorOrganismo getClasificadorOrganismo() {
        return clasificadorOrganismo;
    }

    public void setClasificadorOrganismo(ClasificadorOrganismo clasificadorOrganismo) {
        this.clasificadorOrganismo = clasificadorOrganismo;
    }

    public List<RecursoPropio> getLstRecursoPropio() {
        return lstRecursoPropio;
    }

    public void setLstRecursoPropio(List<RecursoPropio> lstRecursoPropio) {
        this.lstRecursoPropio = lstRecursoPropio;
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
        if (!(object instanceof Organismo)) {
            return false;
        }
        Organismo other = (Organismo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.cg.entity.Organismo[ id=" + id + " ]";
    }

}
