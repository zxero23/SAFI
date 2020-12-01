/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.CierreEjercicio;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Ejercicio;
import com.safi.entity.EstadoEjercicio;
import com.safi.entity.Periodo;
import com.safi.entity.Servicio;
import com.safi.enums.TipoEstadoEnum;


/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class EjercicioFacade extends AbstractFacade<Ejercicio> implements EjercicioFacadeLocal {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EjercicioFacade.class);
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private EstadoEjercicioFacadeLocal estadoEjercicioFacade;
    @EJB
    private PeriodoFacadeLocal periodoFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private CierreEjercicioFacadeLocal cierreEjercicioFacade;

    private final int mes = 11; //Mes del periodo
    private final Long tipoServicioAdministrativo = 1L;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EjercicioFacade() {
        super(Ejercicio.class);
    }

    /**
     * @author Alvarenga Angel
     * @param estado
     * @return List Ejercicio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Ejercicio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Ejercicio as o WHERE o.estado = :p1 order by o.anio");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    /**
     * Este metodo se utiliza para el filtro de
     *
     * @author Matias
     * @param anio
     * @param idEstado
     * @return List Ejercicio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Ejercicio> findAll(Integer anio, Long idEstado) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(o) FROM Ejercicio as o WHERE  o.estado = true");
        if (anio != null && anio != 0) {
            query.append(" AND o.anio =").append(anio);
        }
        if (idEstado != null && idEstado != 0L) {
            query.append(" AND o.estadoEjercicio.id =").append(idEstado);
        }
        query.append(" ORDER BY o.anio DESC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Ejercicio> findAllNuevoBancario(Integer anio) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(o) FROM Ejercicio as o WHERE  o.estado = true");
        if (anio != null && anio != 0) {
            query.append(" AND o.anio =").append(anio);
        }

        query.append(" AND (o.estadoEjercicio.id =").append(TipoEstadoEnum.ACTIVO.getId()).append(" or o.estadoEjercicio.id=").append(TipoEstadoEnum.FORMULACION.getId()+")");
        query.append(" ORDER BY o.anio DESC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Ejercicio> findAll(Integer anio, Long idEstado, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Ejercicio as o WHERE o.estado = true ");
        if (anio != null && anio != 0) {
            query.append(" AND o.anio =").append(anio);
        }
        if (idEstado != null && idEstado != 0L) {
            query.append(" AND o.estadoEjercicio.id =").append(idEstado);
        }
        query.append(" ORDER BY o.anio DESC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Integer anio, Long idEstado) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Ejercicio as o WHERE o.estado = true ");
        if (anio != null && anio != 0) {
            query.append(" AND o.anio =").append(anio);
        }
        if (idEstado != null && idEstado != 0L) {
            query.append(" AND o.estadoEjercicio.id =").append(idEstado);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    /**
     * Crea un nuevo ejercicio y sus 13 periodos, asigna a los servicios
     * administrativos el ejerciccio creado
     *
     * @autor Eugenio
     * @param anio
     * @param fechaComplemento
     * @throws Exception
     */
    @Override
    public void create(Integer anio, Date fechaComplemento) throws Exception {
        try {
            if (this.findAll(anio, null).size() > 0) {
                throw new Exception("El año " + anio + " ya existe.");
            }
            Calendar fechaPrimerDia = new GregorianCalendar(anio + 1, 0, 11);
            Calendar fechaUltimoDia = new GregorianCalendar();
            fechaUltimoDia.setTime(fechaComplemento);
            if (fechaUltimoDia.before(fechaPrimerDia) || fechaUltimoDia.equals(fechaPrimerDia)) {
                throw new Exception("La fecha complementaria debe ser mayor a 11/01/" + (fechaPrimerDia.getTime().getYear() + 1900) + ".");
            }
            Calendar c1 = new GregorianCalendar();
            c1.setTime(fechaComplemento);

            Integer anioComplementario = c1.get(Calendar.YEAR);
            if (anioComplementario <= anio) {
                throw new Exception(" El año en la fecha complementaria debe ser mayor al año ingresado. ");
            }
            /*
            Condicion para evaluar si es el primer dia del año en curso y se esta generado lo guarde en activo
            */
            Calendar diaHoy = new GregorianCalendar();
            int anioActual = Calendar.getInstance().get(Calendar.YEAR);
            EstadoEjercicio estadoEjercicioAux = new EstadoEjercicio();
            if(diaHoy.after(new GregorianCalendar(anioActual,Calendar.JANUARY,01,00,00,00)) && diaHoy.before(new GregorianCalendar(anioActual,Calendar.JANUARY,01,23,59,59))){
                estadoEjercicioAux = this.estadoEjercicioFacade.find(TipoEstadoEnum.ACTIVO.getId());//ver ID  tabla estadoEjercicio = Activo
            }else{
                estadoEjercicioAux = this.estadoEjercicioFacade.find(TipoEstadoEnum.FORMULACION.getId());//ver ID  tabla estadoEjercicio = Formulacion
            }
            Ejercicio ejercicioAux = new Ejercicio();
            ejercicioAux.setAnio(anio);
            ejercicioAux.setEstado(true);
            ejercicioAux.setEstadoEjercicio(estadoEjercicioAux);

            Calendar calendar = new GregorianCalendar(anio, 0, 1);
            ejercicioAux.setFechaInicio(calendar.getTime());
            calendar = new GregorianCalendar(anio + 1, 0, 10);
            ejercicioAux.setFechaFin(calendar.getTime());
            this.create(ejercicioAux);
            /* Creamos automaticamente los 13 periodos*/
            int primerDia, ultimoDia;
            for (int i = 0; i < 11; i++) {

                calendar = new GregorianCalendar(anio, i, 1);
                primerDia = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                fechaPrimerDia = new GregorianCalendar(anio, i, primerDia);
                fechaUltimoDia = new GregorianCalendar(anio, i, ultimoDia);

                Periodo unPeriodo = new Periodo();
                unPeriodo.setEstado(true);
                unPeriodo.setFechaInicio(fechaPrimerDia.getTime());
                unPeriodo.setFechaFin(fechaUltimoDia.getTime());
                unPeriodo.setEjercicio(ejercicioAux);
                this.periodoFacade.create(unPeriodo);

                ejercicioAux.getLstPeriodos().add(unPeriodo);

            }
            /* Periodo 12*/
            fechaPrimerDia = new GregorianCalendar(anio, 11, 1);
            fechaUltimoDia = new GregorianCalendar(anio + 1, 0, 10);
            Periodo unPeriodo = new Periodo();
            unPeriodo.setEstado(true);
            unPeriodo.setFechaInicio(fechaPrimerDia.getTime());
            unPeriodo.setFechaFin(fechaUltimoDia.getTime());
            unPeriodo.setEjercicio(ejercicioAux);
            this.periodoFacade.create(unPeriodo);
            ejercicioAux.getLstPeriodos().add(unPeriodo);
            /* Fin periodo 12 */

            /* Periodo 13 / complementario */
            fechaPrimerDia = new GregorianCalendar(anio + 1, 0, 11);
//            fechaUltimoDia = new GregorianCalendar(anio + 1, 0, ultimoDia);
            fechaUltimoDia.setTime(fechaComplemento);
            unPeriodo = new Periodo();
            unPeriodo.setEstado(true);
            unPeriodo.setFechaInicio(fechaPrimerDia.getTime());
            unPeriodo.setFechaFin(fechaUltimoDia.getTime());
            unPeriodo.setEjercicio(ejercicioAux);
            this.periodoFacade.create(unPeriodo);
            ejercicioAux.getLstPeriodos().add(unPeriodo);
            ejercicioAux.setFechaComplementaria(fechaUltimoDia.getTime());
            this.edit(ejercicioAux);

            /* Agrego al ejercicio los servicios */
            List<Servicio> servicios = this.servicioFacade.findAll(true);
            for (Servicio servicio : servicios) {
                ejercicioAux.getLstServicios().add(servicio);
            }
            this.ejercicioFacade.edit(ejercicioAux);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @Schedule(hour = "0", minute = "10", second = "0", persistent = false) 
    public void postComprobacion() {
        log.info("Inicio evaluacion para actualizar ejercicios automaticamente.");
        try {
            StringBuilder query = new StringBuilder();
            query.append("select distinct * from ejercicios where estado = true and estadoejercicio_id in (?) "
                    + "and anio = (SELECT extract(year from now())) ");
            Query consulta = em.createNativeQuery(query.toString(),Ejercicio.class);
            consulta.setParameter(1,TipoEstadoEnum.FORMULACION.getId());
            List<Ejercicio> ej = consulta.getResultList();
            if(ej.size() > 0){
                for(Ejercicio a:ej){
                    EstadoEjercicio estadoEjercicioAux = this.estadoEjercicioFacade.find(TipoEstadoEnum.ACTIVO.getId());
                    Ejercicio ejercicioAux = this.find(a.getId());
                    ejercicioAux.setEstadoEjercicio(estadoEjercicioAux);
                    this.edit(ejercicioAux);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Long idEjercicio) throws Exception {
        try {
            Ejercicio ejercicioAux = this.find(idEjercicio);
            ejercicioAux.setEstado(false);
            this.edit(ejercicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el ejercicio.");
        }
    }

    @Override
    public void edit(Long idEjercicio, Long idEstadoEjercicio, Date fechaComplemento) throws Exception {
        try {
            EstadoEjercicio estadoEjercicioAux = this.estadoEjercicioFacade.find(idEstadoEjercicio);
            Ejercicio ejercicioAux = this.find(idEjercicio);
            GregorianCalendar fechaPrimerDia = new GregorianCalendar(ejercicioAux.getAnio() + 1, 0, 11);
            if (fechaComplemento.before(fechaPrimerDia.getTime()) || fechaComplemento.equals(fechaPrimerDia.getTime())) {
                throw new Exception("La fecha complementaria debe ser mayor a 11/01/" + (fechaPrimerDia.getTime().getYear() + 1900) + ".");
            }
            if (ejercicioAux.getLstPeriodosOrderByFechaInicio().isEmpty() || ejercicioAux.getLstPeriodosOrderByFechaInicio().size() == 0) {
                throw new Exception("Error al editar.");
            }
            ejercicioAux.setFechaComplementaria(fechaComplemento);
            ejercicioAux.setEstadoEjercicio(estadoEjercicioAux);
            Periodo periodo = ejercicioAux.getLstPeriodosOrderByFechaInicio().get(this.mes);
            periodo.setFechaFin(fechaComplemento);
            this.periodoFacade.edit(periodo);
            this.edit(ejercicioAux);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<String> cerrarEjercicioServAdministrativo(Long idEjercicio, List<Long> lstIdServicios, Long idUsuario) throws Exception {
        try {
            List<String> retorno = new ArrayList<>();
            Collections.sort(lstIdServicios);
            for (Long idServicio : lstIdServicios) {
                Servicio servicioAux = this.servicioFacade.find(idServicio);
                List<CuentaBancaria> cuentas = new ArrayList<>();
                servicioAux.getLstCuentaBnacariaActivas().stream()
                        .filter((c) -> (c.getEjercicio().getId().equals(idEjercicio))).sorted((s1, s2) -> (s1.getNumero().compareTo(s2.getNumero())))
                        .collect(Collectors.toList())
                        .forEach((c) -> {
                            cuentas.add(c);
                        });
                if (!cuentas.isEmpty()) {
                    cuentas.stream().filter((cuentaBancariaAux) -> (!(BigDecimal.ZERO.compareTo(cuentaBancariaAux.getSaldo()) == 0)
                            && (cuentaBancariaAux.getServicio().getTipoServicio() != null
                            && cuentaBancariaAux.getServicio().getTipoServicio().isCierraConCero()))).forEach((cuentaBancariaAux) -> {
                                retorno.add("Servicio: " + cuentaBancariaAux.getServicio().getCodigo() + " - " + cuentaBancariaAux.getServicio().getAbreviatura() + " - NRO. CUENTA: " + cuentaBancariaAux.getNumero().toString() + " - " + cuentaBancariaAux.getDescripcion());
                            });
                } else {
                    throw new Exception("No es posible cerrar el ejercicio porque el servicio no tiene cuenta bancaria asignada.");
                }
                if (retorno.isEmpty()) {
                    this.cierreEjercicioFacade.create(idEjercicio, idServicio, idUsuario);
                }
            }
            return retorno;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<String> cerrarEjercicio(Long idEjercicio, Long idUsuario) throws Exception {
        try {
            List<String> retorno = new ArrayList<>();
            Ejercicio ejercicioAux = this.find(idEjercicio);

            for (Servicio servicioAux : ejercicioAux.getLstServicios().stream()
                    .sorted((s1, s2) -> (Integer.valueOf(s1.getCodigo()).compareTo(Integer.valueOf(s2.getCodigo()))))
                    .collect(Collectors.toList())) {
                if (servicioAux != null && servicioAux.getTipoServicio() != null) {
                    if (servicioAux.getTipoServicio().getId().equals(tipoServicioAdministrativo)) {
                        List<CuentaBancaria> cuentas = new ArrayList<>();
                        servicioAux.getLstCuentaBnacariaActivas().stream()
                                .sorted((s1, s2) -> (s1.getNumero().compareTo(s2.getNumero())))
                                .filter((c) -> (c.getEjercicio().getId().equals(idEjercicio)))
                                .forEach((c) -> {
                                    cuentas.add(c);

                                    if (c.getSaldo().intValue() != 0) {
                                        retorno.add("SERVICIO: " + c.getServicio().getCodigo() + " - " + c.getServicio().getDescripcion() + " - NRO. CUENTA: " + c.getNumero().toString() + " - " + c.getDescripcion());
                                    }
                                });

                    }
                }
            }
            if (!retorno.isEmpty()) {
                return retorno;
            }
            for (Servicio servicioAux : ejercicioAux.getLstServicios()) {
                this.cierreEjercicioFacade.create(idEjercicio, servicioAux.getId(), idUsuario);
            }
            ejercicioAux.setEstadoEjercicio(this.estadoEjercicioFacade.find(2L));
            this.edit(ejercicioAux);
            return retorno;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @author Gonzalez Facundo
     * @param estado
     * @param idEjercicio
     * @return List Ejercicio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAllServ(Long idEjercicio, boolean estado) {
        Query consulta = em.createQuery("select distinct object(o) from Servicio as o join o.ejercicio ejer join ejer.servicio ser  WHERE o.estado = :p1 AND ejer.id=:p2 order by CAST(o.codigo AS BIGINT)");
        consulta.setParameter("p1", estado);
        consulta.setParameter("p2", idEjercicio);
        return consulta.getResultList();
    }

    /**
     * @author Gonzalez Facundo
     * @return idEjercicio actual
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long ejercicioActual() {
        Calendar c2 = new GregorianCalendar();
        Integer anio = c2.get(Calendar.YEAR);
        return this.findAll(anio, 1L).get(0).getId();
    }

    @Override
    public void reOpenExercise(Long idEjercicio, Long idServicio) throws Exception {
        try {
            List<CierreEjercicio> lstCierres = new ArrayList<>();
            lstCierres = this.cierreEjercicioFacade.findAll(idEjercicio, idServicio);
            if (!lstCierres.isEmpty()) {
                for (CierreEjercicio c : lstCierres) {
                    this.cierreEjercicioFacade.remove(c.getId());
                }
            } else {
                throw new Exception("El ejercicio no está cerrado para el servicio con id=" + idServicio);
            }
        } catch (Exception e) {
            throw new Exception("Error al reabrir el ejercicio: " + e.getMessage());
        }
    }

}
