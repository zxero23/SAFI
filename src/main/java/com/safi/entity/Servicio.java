package com.safi.entity;

import com.safi.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name = "servicios")
public class Servicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String abreviatura;
    private String codigo;
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "ejercicios_servicios",
            joinColumns = {
                @JoinColumn(name = "lstservicios_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "ejercicio_id")}
    )
    private Set<Ejercicio> ejercicio = new HashSet<>();

    @ManyToMany
    private List<Organismo> organismos;
    @OneToMany(mappedBy = "servicio")
    private List<CuentaBancaria> lstCuentaBnacaria;
    @OneToMany(mappedBy = "servicio")
    private List<Usuario> usuarios;
    @OneToMany(mappedBy = "servicio")
    private List<RecursoPropio> lstRecursoPropio;
    @OneToOne
    private TipoServicio tipoServicio;
    @OneToMany
    private List<CodigoExpediente> lstCodigoExpediente;
    private boolean estado;//borrado lógico

    public Servicio() {
        this.lstCodigoExpediente = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CodigoExpediente> getLstCodigoExpediente() {
        return lstCodigoExpediente.stream()
                .filter(i -> i.isEstado())
                .sorted(Comparator.comparing(CodigoExpediente::getCodigo))
                .collect(Collectors.toList());
    }

    public void setLstCodigoExpediente(List<CodigoExpediente> lstCodigoExpediente) {
        this.lstCodigoExpediente = lstCodigoExpediente;
    }

    public List<Organismo> getOrganismos() {
        return organismos;
    }

    public List<Organismo> getOrganismosActivos() {
        return organismos.stream().filter((organismo) -> (organismo.isEstado()))
                .sorted(Comparator.comparing(Organismo::getCodigoOrganismo))
                .collect(Collectors.toList());
    }

    public List<Organismo> getOrganismosActivosOrdenadosCodigoAsc() {
        return organismos.stream().filter((organismo) -> (organismo.isEstado())).collect(Collectors.toList());
    }

    public void setOrganismos(List<Organismo> organismos) {
        this.organismos = organismos;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Usuario> getUsuariosActivos() {
        return usuarios.stream().filter((usuario) -> (usuario.isEstado())).collect(Collectors.toList());
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<RecursoPropio> getLstRecursoPropio() {
        return lstRecursoPropio;
    }

    public void setLstRecursoPropio(List<RecursoPropio> lstRecursoPropio) {
        this.lstRecursoPropio = lstRecursoPropio;
    }

    public List<CuentaBancaria> getLstCuentaBnacaria() {
        return lstCuentaBnacaria;
    }

    public List<CuentaBancaria> getLstCuentaBnacariaActivas() {
        return lstCuentaBnacaria.stream().filter((cuenta) -> (cuenta.isEstado())).collect(Collectors.toList());
    }

    public List<CuentaBancaria> getLstCuentaBnacariaActivasOrdenadas() {
        Comparator<CuentaBancaria> compararAnio = Comparator.comparing(x -> ((x.getEjercicio().getAnio())));
        Comparator<CuentaBancaria> compararCuentaBancaria = Comparator.comparing(x -> x.getNumero());

        return lstCuentaBnacaria.stream().filter((cuenta) -> (cuenta.isEstado())).
                sorted(compararAnio.reversed().thenComparing(compararCuentaBancaria)).
                collect(Collectors.toList());
    }

    public void setLstCuentaBnacaria(List<CuentaBancaria> lstCuentaBnacaria) {
        this.lstCuentaBnacaria = lstCuentaBnacaria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoAbreviatura() {
        return codigo + " - " + abreviatura;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
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
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.cg.entity.Servicio[ id=" + id + " ]";
    }

    public Set<Ejercicio> getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Set<Ejercicio> ejercicio) {
        this.ejercicio = ejercicio;
    }

}
