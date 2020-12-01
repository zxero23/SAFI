/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.entity;

import java.util.ArrayList;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import javax.persistence.OneToOne;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name = "clasificadores_recursos_propios")
public class ClasificadorRecursoPropio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long codigo;
    private String nombre;
    @OneToOne
    private TipoClasificadorRecursoPropio tipoClasificadorRecursoPropio;
    @OneToMany(mappedBy = "clasificadorRecursoPropio")
    private List<ClasificadorRecursoPropio> clasificadoresRecursoPropio;
    private boolean estado;
    @ManyToOne
    private ClasificadorRecursoPropio clasificadorRecursoPropio;

    public ClasificadorRecursoPropio() {
        this.setClasificadoresRecursoPropio(new ArrayList<ClasificadorRecursoPropio>());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCodigo() {
        return this.codigo;
    }

    public void setCodigo(final Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public TipoClasificadorRecursoPropio getTipoClasificadorRecursoPropio() {
        return this.tipoClasificadorRecursoPropio;
    }

    public void setTipoClasificadorRecursoPropio(final TipoClasificadorRecursoPropio tipoClasificadorRecursoPropio) {
        this.tipoClasificadorRecursoPropio = tipoClasificadorRecursoPropio;
    }

    public boolean isEstado() {
        return this.estado;
    }

    public void setEstado(final boolean estado) {
        this.estado = estado;
    }

    public List<ClasificadorRecursoPropio> getClasificadoresRecursoPropio() {
        return this.clasificadoresRecursoPropio.stream().filter(recursoaux -> recursoaux.isEstado())
                .sorted(Comparator.comparing(ClasificadorRecursoPropio::getCodigo))
                .collect(Collectors.toList());
    }

    public void setClasificadoresRecursoPropio(final List<ClasificadorRecursoPropio> clasificadoresRecursoPropio) {
        this.clasificadoresRecursoPropio = clasificadoresRecursoPropio;
    }

    public ClasificadorRecursoPropio getClasificadorRecursoPropio() {
        return this.clasificadorRecursoPropio;
    }

    public void setClasificadorRecursoPropio(final ClasificadorRecursoPropio clasificadorRecursoPropio) {
        this.clasificadorRecursoPropio = clasificadorRecursoPropio;
    }

    public String obtenerCodigoCompleto(final ClasificadorRecursoPropio clasificadorRecursoPropio) {
        String retorno;
        if (clasificadorRecursoPropio.getCodigo() != null) {
            retorno = clasificadorRecursoPropio.getCodigo().toString();
        } else {
            retorno = "XX";
        }
        if (clasificadorRecursoPropio.getClasificadorRecursoPropio() != null) {
            retorno = this.obtenerCodigoCompleto(clasificadorRecursoPropio.getClasificadorRecursoPropio()).concat(".").concat(retorno);
        }
        return retorno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((this.id != null) ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof ClasificadorRecursoPropio)) {
            return false;
        }
        final ClasificadorRecursoPropio other = (ClasificadorRecursoPropio) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.safi.entity.ClasificadorRecursoPropio[ id=" + this.id + " ]";
    }
}
