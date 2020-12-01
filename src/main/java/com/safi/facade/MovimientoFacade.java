/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import com.safi.entity.Usuario;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Ejercicio;
import com.safi.entity.Expediente;
import com.safi.entity.Funcion;
import com.safi.entity.Movimiento;
import com.safi.entity.RecursoPropio;

/**
 *
 * author Gonzalez Facundo ,Angel Alvarenga
 */
@Stateless
public class MovimientoFacade extends AbstractFacade<Movimiento> implements MovimientoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    private FuncionFacadeLocal funcionFacade;
    @EJB
    private ExpedienteFacadeLocal expedienteFacade;
    @EJB
    private SaldoAcumuladoFacadeLocal saldoAcumuladoFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private CierreEjercicioFacadeLocal cierreEjercicioFacade;
    @EJB
    private RecursoPropioFacadeLocal recursoPropioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MovimientoFacade() {
        super(Movimiento.class);
    }

    /**
     * author Alvarenga Angel
     *
     * @param estado
     * @return List Moviviento
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Movimiento> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Movimiento as o WHERE o.estado = :p1 ORDER BY o.fechaAlta DESC");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    /**
     * author Gonzalez Facundo,Alvarenga Angel
     *
     * @param idEjercicio
     * @param idServicio
     * @param idCuentaBancaria
     * @param descripcion
     * @param fechaAltaDesde
     * @param fechaAltaHasta
     * @param fechaComprobDesde
     * @param fechaComprobHasta
     * @param importeDesde
     * @param importeHasta
     * @param codigoOrganismo
     * @param numero
     * @param anio
     * @return
     */
    @Override
    public List<Movimiento> findAll(Long idEjercicio, Long idServicio, Long idCuentaBancaria,
            String descripcion, Date fechaAltaDesde, Date fechaAltaHasta,
            Date fechaComprobDesde, Date fechaComprobHasta, BigDecimal importeDesde,
            BigDecimal importeHasta, Long codigoOrganismo, Long numero, Long anio) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Movimiento as o WHERE  o.estado = true");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id =").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.cuentaBancaria.servicio.id =").append(idServicio);
        }
        if (idCuentaBancaria != null && idCuentaBancaria != 0L) {
            query.append(" and o.cuentaBancaria.id =").append(idCuentaBancaria);
        }
        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and o.descripcion LIKE '%").append(descripcion.toUpperCase()).append("%'");
        }
        if (codigoOrganismo != null && codigoOrganismo != 0L) {
            query.append(" and o.expediente.codigo =").append(codigoOrganismo);
        }
        if (numero != null && numero != -1L) {
            query.append(" and o.expediente.numero =").append(numero);
        }
        if (anio != null && anio != 0L) {
            query.append(" and o.expediente.anio =").append(anio);
        }
        if (importeDesde != null) {
            query.append("   AND o.importe >= ").append(importeDesde);
        }
        if (importeHasta != null) {
            query.append("   AND o.importe <= ").append(importeHasta);
        }
        if (fechaAltaDesde != null) {
            query.append(" and o.fechaAlta  >= :startAlta");
        }
        if (fechaAltaHasta != null) {
            query.append(" and o.fechaAlta  <= :endAlta");
        }
        if (fechaComprobDesde != null) {
            query.append(" and o.fechaComprobante >= :startComp");
        }
        if (fechaComprobHasta != null) {
            query.append(" and o.fechaComprobante <= :endComp");
        }

        query.append(" ORDER BY o.fechaAlta DESC");

        Query consulta = em.createQuery(query.toString());
        if (fechaAltaDesde != null) {
            fechaAltaDesde.setHours(0);
            fechaAltaDesde.setMinutes(0);
            fechaAltaDesde.setSeconds(0);
            consulta.setParameter("startAlta", fechaAltaDesde, TemporalType.TIMESTAMP);
        }
        if (fechaAltaHasta != null) {
            fechaAltaHasta.setHours(23);
            fechaAltaHasta.setMinutes(59);
            fechaAltaHasta.setSeconds(59);
            consulta.setParameter("endAlta", fechaAltaHasta, TemporalType.TIMESTAMP);
        }
        if (fechaComprobDesde != null) {
            fechaComprobDesde.setHours(0);
            fechaComprobDesde.setMinutes(0);
            fechaComprobDesde.setSeconds(0);
            consulta.setParameter("startComp", fechaComprobDesde, TemporalType.TIMESTAMP);
        }
        if (fechaComprobHasta != null) {
            fechaComprobHasta.setHours(23);
            fechaComprobHasta.setMinutes(59);
            fechaComprobHasta.setSeconds(59);
            consulta.setParameter("endComp", fechaComprobHasta, TemporalType.TIMESTAMP);
        }

        return consulta.getResultList();
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @param idEjercicio
     * @param idServicio
     * @param idCuentaBancaria
     * @param descripcion
     * @param fechaAltaDesde
     * @param first
     * @param fechaComprobDesde
     * @param fechaComprobHasta
     * @param importeDesde
     * @param importeHasta
     * @param codigoOrganismo
     * @param numero
     * @param anio
     * @param funcionAux
     * @param fechaAltaHasta
     * @param pageSize
     * @param numeroOrdenBsq
     * @param numeroPedidoFondoBsq
     * @param numeroEntregaFondoBsq
     * @return List Movimiento
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Movimiento> findAll(Long idEjercicio, Long idServicio, Long idCuentaBancaria,
            String descripcion, Date fechaAltaDesde, Date fechaAltaHasta,
            Date fechaComprobDesde, Date fechaComprobHasta, BigDecimal importeDesde,
            BigDecimal importeHasta, Long codigoOrganismo, Long numero, Long anio,
            Long numeroOrdenBsq, Long numeroPedidoFondoBsq, Long numeroEntregaFondoBsq, Long tipoOrdenGasto, Long tipoFondo, Long tipoImputacion, int first, int pageSize) {

        StringBuilder query = new StringBuilder();
        query.append("select DISTINCT(object(o)) FROM Movimiento as o,o.funciones as f WHERE  o.estado = true");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id =").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.cuentaBancaria.servicio.id =").append(idServicio);
        }
        if (idCuentaBancaria != null && idCuentaBancaria != 0L) {
            query.append(" and o.cuentaBancaria.id =").append(idCuentaBancaria);
        }
        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and o.descripcion LIKE '%").append(descripcion.toUpperCase()).append("%'");
        }
        if (codigoOrganismo != null && codigoOrganismo != 0L) {
            query.append(" and o.expediente.codigo =").append(codigoOrganismo);
        }
        if (numero != null && numero != -1L) {
            query.append(" and o.expediente.numero =").append(numero);
        }
        if (anio != null && anio != 0L) {
            query.append(" and o.expediente.anio =").append(anio);
        }
        if (importeDesde != null) {
            query.append("   AND o.importe >= ").append(importeDesde);;
        }

        if (importeHasta != null) {
            query.append("   AND o.importe <= ").append(importeHasta);
        }

        if (fechaAltaDesde != null) {
            query.append(" and o.fechaAlta  >= :startAlta");
        }
        if (fechaAltaHasta != null) {
            query.append(" and o.fechaAlta  <= :endAlta");
        }
        if (fechaComprobDesde != null) {
            query.append(" and o.fechaComprobante >= :startComp");
        }
        if (fechaComprobHasta != null) {
            query.append(" and o.fechaComprobante <= :endComp");
        }
        if (tipoOrdenGasto != null && tipoOrdenGasto != 0L) {
            query.append(" and f.tipoOrdenGasto.id =").append(tipoOrdenGasto);
        }
        if (tipoFondo != null && tipoFondo != 0L) {
            query.append(" and f.tipoFondo.id =").append(tipoFondo);
        }
        if (tipoImputacion != null && tipoImputacion != 0L) {
            query.append(" and f.tipoImputacion.id =").append(tipoImputacion);
        }

        if (numeroOrdenBsq != null) {
            query.append(" and o.numeroOrden = ").append(numeroOrdenBsq);
        }

        if (numeroPedidoFondoBsq != null) {
            query.append(" and o.numeroPedidoFondo = ").append(numeroPedidoFondoBsq);
        }

        if (numeroEntregaFondoBsq != null) {
            query.append(" and o.numeroEntregaFondo = ").append(numeroEntregaFondoBsq);
        }
        query.append(" ORDER BY o.ejercicio.anio DESC,o.cuentaBancaria.servicio.id ASC,o.numeroAsiento DESC");
        Query consulta = em.createQuery(query.toString());
        if (fechaAltaDesde != null) {
            fechaAltaDesde.setHours(0);
            fechaAltaDesde.setMinutes(0);
            fechaAltaDesde.setSeconds(0);
            consulta.setParameter("startAlta", fechaAltaDesde, TemporalType.TIMESTAMP);
        }
        if (fechaAltaHasta != null) {
            fechaAltaHasta.setHours(23);
            fechaAltaHasta.setMinutes(59);
            fechaAltaHasta.setSeconds(59);
            consulta.setParameter("endAlta", fechaAltaHasta, TemporalType.TIMESTAMP);
        }
        if (fechaComprobDesde != null) {
            fechaComprobDesde.setHours(0);
            fechaComprobDesde.setMinutes(0);
            fechaComprobDesde.setSeconds(0);
            consulta.setParameter("startComp", fechaComprobDesde, TemporalType.TIMESTAMP);
        }
        if (fechaComprobHasta != null) {
            fechaComprobHasta.setHours(23);
            fechaComprobHasta.setMinutes(59);
            fechaComprobHasta.setSeconds(59);
            consulta.setParameter("endComp", fechaComprobHasta, TemporalType.TIMESTAMP);
        }

        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();

    }

    /**
     * author Alvarenga Angel
     *
     * @param idEjercicio
     * @param idServicio
     * @param idCuentaBancaria
     * @param descripcion
     * @param fechaAltaDesde
     * @param fechaAltaHasta
     * @param fechaComprobDesde
     * @param fechaComprobHasta
     * @param importeDesde
     * @param importeHasta
     * @param codigoOrganismo
     * @param numero
     * @param anio
     * @param funcionAux
     * @param numeroOrdenBsq
     * @param numeroPedidoFondoBsq
     * @param numeroEntregaFondoBsq
     * @return List Moviviento
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idEjercicio, Long idServicio, Long idCuentaBancaria,
            String descripcion, Date fechaAltaDesde, Date fechaAltaHasta,
            Date fechaComprobDesde, Date fechaComprobHasta, BigDecimal importeDesde,
            BigDecimal importeHasta, Long codigoOrganismo, Long numero, Long anio,
            Long numeroOrdenBsq, Long numeroPedidoFondoBsq, Long numeroEntregaFondoBsq, Long tipoOrdenGasto, Long tipoFondo, Long tipoImputacion) {

        StringBuilder query = new StringBuilder();
        query.append("select COUNT(DISTINCT(o)) FROM Movimiento as o,o.funciones as f WHERE  o.estado = true");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id =").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.cuentaBancaria.servicio.id =").append(idServicio);
        }
        if (idCuentaBancaria != null && idCuentaBancaria != 0L) {
            query.append(" and o.cuentaBancaria.id =").append(idCuentaBancaria);
        }
        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and o.descripcion LIKE '%").append(descripcion.toUpperCase()).append("%'");
        }
        if (codigoOrganismo != null && codigoOrganismo != 0L) {
            query.append(" and o.expediente.codigo =").append(codigoOrganismo);
        }
        if (numero != null && numero != -1L) {
            query.append(" and o.expediente.numero =").append(numero);
        }
        if (anio != null && anio != 0L) {
            query.append(" and o.expediente.anio =").append(anio);
        }
        if (importeDesde != null) {
            query.append("   AND o.importe >= ").append(importeDesde);
        }
        if (importeHasta != null) {
            query.append("   AND o.importe <= ").append(importeHasta);
        }
        if (fechaAltaDesde != null) {
            query.append(" and o.fechaAlta  >= :startAlta");
        }
        if (fechaAltaHasta != null) {
            query.append(" and o.fechaAlta  <= :endAlta");
        }
        if (fechaComprobDesde != null) {
            query.append(" and o.fechaComprobante >= :startComp");
        }
        if (fechaComprobHasta != null) {
            query.append(" and o.fechaComprobante <= :endComp");
        }

        if (tipoOrdenGasto != null && tipoOrdenGasto != 0L) {
            query.append(" and f.tipoOrdenGasto.id =").append(tipoOrdenGasto);
        }
        if (tipoFondo != null && tipoFondo != 0L) {
            query.append(" and f.tipoFondo.id =").append(tipoFondo);
        }
        if (tipoImputacion != null && tipoImputacion != 0L) {
            query.append(" and f.tipoImputacion.id =").append(tipoImputacion);
        }

        if (numeroOrdenBsq != null) {
            query.append(" and o.numeroOrden = ").append(numeroOrdenBsq);
        }

        if (numeroPedidoFondoBsq != null) {
            query.append(" and o.numeroPedidoFondo =").append(numeroPedidoFondoBsq);
        }

        if (numeroEntregaFondoBsq != null) {
            query.append(" and o.numeroEntregaFondo =").append(numeroEntregaFondoBsq);
        }

        Query consulta = em.createQuery(query.toString());
        if (fechaAltaDesde != null) {
            fechaAltaDesde.setHours(0);
            fechaAltaDesde.setMinutes(0);
            fechaAltaDesde.setSeconds(0);
            consulta.setParameter("startAlta", fechaAltaDesde, TemporalType.TIMESTAMP);
        }
        if (fechaAltaHasta != null) {
            fechaAltaHasta.setHours(23);
            fechaAltaHasta.setMinutes(59);
            fechaAltaHasta.setSeconds(59);
            consulta.setParameter("endAlta", fechaAltaHasta, TemporalType.TIMESTAMP);
        }
        if (fechaComprobDesde != null) {
            fechaComprobDesde.setHours(0);
            fechaComprobDesde.setMinutes(0);
            fechaComprobDesde.setSeconds(0);
            consulta.setParameter("startComp", fechaComprobDesde, TemporalType.TIMESTAMP);
        }
        if (fechaComprobHasta != null) {
            fechaComprobHasta.setHours(23);
            fechaComprobHasta.setMinutes(59);
            fechaComprobHasta.setSeconds(59);
            consulta.setParameter("endComp", fechaComprobHasta, TemporalType.TIMESTAMP);
        }

        return (Long) consulta.getSingleResult();

    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @param idEjercicio
     * @param idCuentaBancaria
     * @param idTipoOrdenGasto
     * @param idTipoFondo
     * @param idTipoImputacion
     * @param nroOrden
     * @param nroPedidoFondo
     * @param nroEntregaFondo
     * @param codigo
     * @param numero
     * @param anio
     * @param nroComprobante
     * @param fechaComprobante
     * @param descripcion
     * @param importe
     * @param usuario
     * @param idRecursoPropio
     * @throws Exception
     */
    @Override
    public void create(Long idEjercicio, Long idCuentaBancaria,
            Long idTipoOrdenGasto, Long idTipoFondo, Long idTipoImputacion,
            Long nroOrden, Long nroPedidoFondo, Long nroEntregaFondo, Long codigo,
            Long numero, Long anio, Long nroComprobante, Date fechaComprobante,
            String descripcion, BigDecimal importe, Usuario usuario, Long idRecursoPropio, boolean revertido) throws Exception {
        try {
            Usuario usuarioAux = usuario;
            Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
            CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(idCuentaBancaria);
            /* Evalúo que no se haya cerrado el ejercicio para servicio administrativo */
            if (this.cierreEjercicioFacade.findAll(idEjercicio, (usuarioAux.getServicio() == null ? 0L : usuarioAux.getServicio().getId())).isEmpty()) {
                /* Evalúo el ejercicio no haya cerrado */
                if (ejercicioAux.getEstadoEjercicio().getId().equals(1L)) { //1L estado activo ver DB
                    List<Funcion> lstFuncionAux = this.funcionFacade.findAll(idTipoOrdenGasto, idTipoFondo, idTipoImputacion, 0L);
                    Expediente expedienteAux = new Expediente();
                    expedienteAux.setCodigo(codigo);
                    expedienteAux.setNumero(numero);
                    expedienteAux.setAnio(anio);
                    this.expedienteFacade.create(expedienteAux);
                    Movimiento movimientoAux = new Movimiento();
                    movimientoAux.setNumeroAsiento(this.generarNumeroAsiento(cuentaBancariaAux.getServicio().getId(), idEjercicio));
                    movimientoAux.setFechaAlta(new Date());

                    if (nroOrden != null) {
                        movimientoAux.setNumeroOrden(nroOrden);
                    } else {
                        movimientoAux.setNumeroOrden(0L);
                    }

                    if (nroPedidoFondo != null) {
                        movimientoAux.setNumeroPedidoFondo(nroPedidoFondo);
                    } else {
                        movimientoAux.setNumeroPedidoFondo(0L);
                    }

                    if (nroEntregaFondo != null) {
                        movimientoAux.setNumeroEntregaFondo(nroEntregaFondo);
                    } else {
                        movimientoAux.setNumeroEntregaFondo(0L);
                    }
                    if (idRecursoPropio != null || idRecursoPropio != 0L) {
                        RecursoPropio recursoAux = this.recursoPropioFacade.find(idRecursoPropio);
                        movimientoAux.setRecursoPropio(recursoAux);
                    }
                    movimientoAux.setNumeroComprobante(nroComprobante);
                    movimientoAux.setFechaComprobante(fechaComprobante);
                    movimientoAux.setDescripcion(descripcion.toUpperCase());
                    movimientoAux.setImporte(importe);
                    movimientoAux.setExpediente(expedienteAux);
                    movimientoAux.setEstado(true);
                    movimientoAux.setCuentaBancaria(cuentaBancariaAux);
                    movimientoAux.setEjercicio(ejercicioAux);
                    movimientoAux.setUsuario(usuarioAux);
                    movimientoAux.setFunciones(lstFuncionAux); //seteamos lista de funciones 23-01-2019
                    movimientoAux.setRevertido(revertido);
                    if (idTipoOrdenGasto == null || idTipoFondo == null || idTipoImputacion == null) {
                        throw new Exception("No se pudo efectuar el movimiento la combinación Orden-Fondo-imputación es incorrecta");
                    }
                    if (lstFuncionAux != null) {
                        this.create(movimientoAux);
                    } else {
                        throw new Exception("Error tipo de Función, por favor genere el movimiento nuevamente.");
                    }

                    //actualizar saldo de la cuenta                    
                    saldoAcumuladoFacade.actualizarSaldoCuentaBancaria(cuentaBancariaAux, importe, lstFuncionAux);
                } else {
                    throw new Exception("El ejercicio ha sido cerrado, no se pueden generar nuevos movimientos.");
                }
            } else {
                throw new Exception("El ejercicio ha sido cerrado para el servicio al que pertenece.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el movimiento: " + e.getMessage());
        }
    }

    /**
     * author Gonzalez Facundo
     *
     * @param orgExpediente
     * @param idEjercicio
     * @return Long
     */
    private Long generarNumeroAsiento(Long servicioId, Long idEjercicio) {
        Long numeroAsientoAux;
        try {
            Query consulta = em.createQuery("SELECT MAX(m.numeroAsiento) FROM Movimiento m WHERE m.cuentaBancaria.servicio.id = " + servicioId + " AND m.ejercicio.id = " + idEjercicio);
            numeroAsientoAux = (Long) consulta.getSingleResult();
            ++numeroAsientoAux;
        } catch (Exception e) {
            numeroAsientoAux = 1L;
        }
        return numeroAsientoAux;
    }

    /**
     * author Zakowicz Matias
     *
     * @param idCuentaBancaria Listado (Ultimos 10) de movimientos de una cuenta
     * bancaria ordenado por fecha.
     * @return List Moviviento
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Movimiento> findByCuentaBancaria(Long idCuentaBancaria) {
        Query consulta = em.createQuery("select object(o) from Movimiento as o WHERE o.estado = true and o.cuentaBancaria.id = :p1 order by o.fechaAlta desc");
        consulta.setParameter("p1", idCuentaBancaria);
        consulta.setMaxResults(10);
        /*Apara que retorne X cantidad de resultados*/

        return consulta.getResultList();
    }

    @Override
    public List<Long> findAllCodigoOrganismo(Long idEjercicio, Long idCuenta) {
        Query consulta = em.createQuery("select distinct(m.expediente.codigo) from Movimiento as m WHERE m.estado = true AND m.ejercicio.id = :p1 AND m.cuentaBancaria.id = :p2 order by m.expediente.codigo");
        consulta.setParameter("p1", idEjercicio);
        consulta.setParameter("p2", idCuenta);
        return (List<Long>) consulta.getResultList();
    }

    /**
     * author Gonzalez Facundo
     *
     * @param idSaldoAcumulado Saldos acumulados
     *
     * @return List Moviviento
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Movimiento> findSaldoByCuentaBancaria(Long idSaldoAcumulado) {
        Query consulta = em.createQuery("select object(o) from Movimiento as o WHERE o.estado = true and o.cuentaBancaria.id = :p1 order by o.fechaAlta desc");
        consulta.setParameter("p1", idSaldoAcumulado);
        //consulta.setMaxResults(10); /*Apara que retorne X cantidad de resultados*/

        return consulta.getResultList();
    }

    /**
     * author Gonzalez Facundo
     *
     * devuelve numeros de expedientes segun codigo
     *
     * @param idEjercicio
     * @param idCuenta
     * @param codigo
     * @return
     */
    @Override
    public List<Long> findAllExpedientes(Long idEjercicio, Long idCuenta, Long codigo) {

        Query consulta = em.createQuery("select distinct(m.expediente.numero) from Movimiento as m WHERE m.estado = true AND m.ejercicio.id = :p1 "
                + " AND m.expediente.codigo = :p3 "
                + " AND m.cuentaBancaria.id = :p2 "
                + "order by m.expediente.numero");
        consulta.setParameter("p1", idEjercicio);
        consulta.setParameter("p2", idCuenta);
        consulta.setParameter("p3", codigo);
        return (List<Long>) consulta.getResultList();
    }

}
