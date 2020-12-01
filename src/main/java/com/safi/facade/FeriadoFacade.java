/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Feriado;

/**
 *
 * @author Doroñuk Gustavo
 */
@Stateless
public class FeriadoFacade extends AbstractFacade<Feriado> implements FeriadoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {

        return em;

    }

    public FeriadoFacade() {

        super(Feriado.class);

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Feriado> findAll(boolean estado) {

        Query consulta = em.createQuery("SELECT object(f) FROM Feriado AS f WHERE f.estado = :p1 ORDER BY f.fecha DESC");
        consulta.setParameter("p1", estado);

        return consulta.getResultList();

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Feriado> findAll(String nombreBuscar, Date fechaDesdeBuscar, Date fechaHastaBuscar) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(f) FROM Feriado AS f WHERE f.estado = true ");
        if (!(nombreBuscar.equals(""))) {
            query.append(" AND f.nombre LIKE '%").append(nombreBuscar).append("%'");
        }
        if (fechaDesdeBuscar != null) {
            Timestamp fDesde = new Timestamp(fechaDesdeBuscar.getTime());
            fDesde.setHours(0);
            fDesde.setMinutes(0);
            fDesde.setSeconds(0);
            query.append(" AND f.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHastaBuscar != null) {
            Timestamp fHasta = new Timestamp(fechaHastaBuscar.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append(" AND f.fecha <= '").append(fHasta).append("' ");
        }
        query.append(" ORDER BY f.fecha DESC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Feriado> findAll(String nombreBuscar, Date fechaDesdeBuscar, Date fechaHastaBuscar, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(f) FROM Feriado AS f WHERE f.estado = true ");
        if (nombreBuscar!=null && !(nombreBuscar.equals(""))) {
            query.append(" AND f.nombre LIKE '%").append(nombreBuscar).append("%'");
        }
        if (fechaDesdeBuscar != null) {
            Timestamp fDesde = new Timestamp(fechaDesdeBuscar.getTime());
            fDesde.setHours(0);
            fDesde.setMinutes(0);
            fDesde.setSeconds(0);
            query.append(" AND f.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHastaBuscar != null) {
            Timestamp fHasta = new Timestamp(fechaHastaBuscar.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append(" AND f.fecha <= '").append(fHasta).append("' ");
        }
        query.append(" ORDER BY f.fecha DESC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(String nombreBuscar, Date fechaDesdeBuscar, Date fechaHastaBuscar) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT count(f) FROM Feriado AS f WHERE f.estado = true ");
        if (nombreBuscar!=null && !(nombreBuscar.equals(""))) {
            query.append(" AND f.nombre LIKE '%").append(nombreBuscar).append("%'");
        }
        if (fechaDesdeBuscar != null) {
            Timestamp fDesde = new Timestamp(fechaDesdeBuscar.getTime());
            fDesde.setHours(0);
            fDesde.setMinutes(0);
            fDesde.setSeconds(0);
            query.append(" AND f.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHastaBuscar != null) {
            Timestamp fHasta = new Timestamp(fechaHastaBuscar.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append(" AND f.fecha <= '").append(fHasta).append("' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Feriado> findFeriados(List<Long> idFeriados) {

        String iDes = idFeriados.toString().substring(1, idFeriados.toString().length() - 1);

        return em.createQuery("SELECT object(f) FROM Feriado AS f WHERE f.id IN (" + iDes + " ) ").getResultList();

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Feriado> findAllFeriadoLogin(boolean estado) {

        Query consulta = em.createQuery("SELECT object(f) FROM Feriado AS f WHERE f.fecha > CURRENT_DATE and f.estado = :p1 ORDER BY f.fecha ASC").setMaxResults(3);
        consulta.setParameter("p1", estado);
        return consulta.getResultList();

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public boolean isFeriado(Date fecha) {

        try {

            if (fecha != null) {

                LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                StringBuilder query = new StringBuilder();
                query.append("SELECT object(f) FROM Feriado AS f WHERE EXTRACT(YEAR FROM f.fecha)=").append(localDate.getYear());
                query.append(" and EXTRACT(MONTH FROM f.fecha)=").append(localDate.getMonthValue());
                query.append(" and EXTRACT(DAY FROM f.fecha)=").append(localDate.getDayOfMonth());
                Query consulta = em.createQuery(query.toString());
                List<Feriado> feriados = consulta.getResultList();
                return !feriados.isEmpty();

            } else {

                return false;

            }

        } catch (Exception e) {

            return false;

        }

    }

    @Override
    public void create(String nombre, Date fecha) throws Exception {

        try {

            Feriado feriado = new Feriado();
            feriado.setEstado(true);
            feriado.setNombre(nombre.toUpperCase());
            feriado.setFecha(fecha);
            this.create(feriado);

        } catch (Exception e) {

            throw new Exception("Error al intentar crear el feriado: " + e.getMessage());

        }

    }

    @Override
    public void remove(Long idFeriado) throws Exception {

        try {

            Feriado feriadoAux = this.find(idFeriado);
            feriadoAux.setEstado(false);
            this.edit(feriadoAux);

        } catch (Exception e) {

            throw new Exception("Error al intentar borrar el feriado: " + e.getMessage());

        }

    }

    @Override
    public void edit(Long idFeriado, String nombre, Date fecha) throws Exception {

        try {

            Feriado feriadoAux = this.find(idFeriado);
            feriadoAux.setNombre(nombre.toUpperCase());
            feriadoAux.setFecha(fecha);
            this.edit(feriadoAux);

        } catch (Exception e) {

            throw new Exception("Error al intentar editar el feriado: " + e.getMessage());

        }

    }

}
