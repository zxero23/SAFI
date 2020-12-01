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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import com.safi.entity.Usuario;

/**
 *
 * @author Angel Alvarenga
 */
@Entity
@Table(name="movimientos")
public class Movimiento implements Serializable {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="numero_asiento")
    private Long numeroAsiento;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fecha_alta")
    private Date fechaAlta;
    @Column(name="numero_orden")
    private Long numeroOrden;
    @Column(name="numero_pedido_fondo")
    private Long numeroPedidoFondo;
    @Column(name="numero_entrega_fondo")
    private Long numeroEntregaFondo;
    @Column(name="numero_comprobante")
    private Long numeroComprobante;
    @Column(name="fecha_comprobante")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaComprobante;
    @Column(name="extracto_comprobante")
    private String extractoComprobante;
    private String descripcion;//extracto
    @Column (name="importe", precision=14, scale=2)
    private BigDecimal importe;
    @OneToOne(fetch=FetchType.EAGER)
    private Expediente expediente;
    @ManyToOne(fetch=FetchType.EAGER)
    private RecursoPropio recursoPropio;
    @OneToOne
    private Periodo periodo;
    @OneToOne
    private Ejercicio ejercicio;
    @OneToOne
    private Usuario usuario;
    private boolean estado;//borrado lógico
    @ManyToOne
    private CuentaBancaria cuentaBancaria;
    /*
     * Atributo provisorio para los validar que los movimientos tengan su respectivo documento presentado, ver edición de movimiento.
     * Matias Zakowicz 16-01-2019
    */
    @Column(name = "documento_presentado")
    private boolean documentoPresentado;
    //lista de funciones 23/01/2019
    //Facundo Gonzalez
    @OneToMany
    private List<Funcion> funciones;
    private boolean revertido;
    
    
    public Movimiento() {
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Long numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Long getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Long numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Long getNumeroPedidoFondo() {
        return numeroPedidoFondo;
    }

    public void setNumeroPedidoFondo(Long numeroPedidoFondo) {
        this.numeroPedidoFondo = numeroPedidoFondo;
    }

    public Long getNumeroEntregaFondo() {
        return numeroEntregaFondo;
    }

    public void setNumeroEntregaFondo(Long numeroEntregaFondo) {
        this.numeroEntregaFondo = numeroEntregaFondo;
    }

    public Long getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(Long numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Date getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(Date fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getExtractoComprobante() {
        return extractoComprobante;
    }

    public void setExtractoComprobante(String extractoComprobante) {
        this.extractoComprobante = extractoComprobante;
    }

    public RecursoPropio getRecursoPropio() {
        return recursoPropio;
    }

    public void setRecursoPropio(RecursoPropio recursoPropio) {
        this.recursoPropio = recursoPropio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isDocumentoPresentado() {
        return documentoPresentado;
    }

    public void setDocumentoPresentado(boolean documentoPresentado) {
        this.documentoPresentado = documentoPresentado;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<Funcion> funciones) {
        this.funciones = funciones;
    }

    public boolean isRevertido() {
        return revertido;
    }

    public void setRevertido(boolean revertido) {
        this.revertido = revertido;
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
        if (!(object instanceof Movimiento)) {
            return false;
        }
        Movimiento other = (Movimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.cg.entity.Movimiento[ id=" + id + " ]";
    }
    
}
