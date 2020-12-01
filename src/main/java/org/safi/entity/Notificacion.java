package org.safi.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.safi.entity.Usuario;

/**
 * @author Doroñuk Gustavo
 */
@Entity
@Table(name = "notificaciones")
public class Notificacion implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Usuario usuarioEmisor;
    private String mensaje;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private boolean leido;
    @ManyToMany(mappedBy = "notificacion", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Servicio> servicio = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(Usuario usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
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
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.safi.entity.Notificacion[ id=" + id + " ]";
    }

    public Set<Servicio> getServicio() {
        return servicio;
    }

    public void setServicio(List<Servicio> servicio) {
        Set<Servicio> hSet = new HashSet<Servicio>(); 
        for (Servicio x : servicio) 
            hSet.add(x); 
        
    }
    
    
    
}