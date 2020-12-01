/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Periodo;

/**
 *
 * @author eugenio
 */
@Stateless
public class PeriodoFacade extends AbstractFacade<Periodo> implements PeriodoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeriodoFacade() {
        super(Periodo.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Periodo> findAll(Date fechaDesde, Date fechaHasta) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(o) FROM Periodo AS o WHERE o.estado = true ");

        if (fechaDesde != null) {
            Timestamp fDesde = new Timestamp(fechaDesde.getTime());
            query.append(" AND o.fechaInicio >= '").append(fDesde).append("' ");
        }
        if (fechaHasta != null) {
            Timestamp fHasta = new Timestamp(fechaHasta.getTime());
            query.append(" AND o.fechaFin <= '").append(fHasta).append("' ");
        }

        query.append(" ORDER BY o.fechaInicio ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Periodo> findAll(Date fechaDesde, Date fechaHasta, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Periodo as o WHERE o.estado = true ");

        if (fechaDesde != null) {
            Timestamp fDesde = new Timestamp(fechaDesde.getTime());
            query.append(" AND o.fechaInicio >= '").append(fDesde).append("' ");
        }
        if (fechaHasta != null) {
            Timestamp fHasta = new Timestamp(fechaHasta.getTime());
            query.append(" AND o.fechaFin <= '").append(fHasta).append("' ");
        }

        query.append(" ORDER BY o.fechaInicio ASC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Date fechaDesde, Date fechaHasta) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Periodo as o WHERE o.estado = true ");

        if (fechaDesde != null) {
            Timestamp fDesde = new Timestamp(fechaDesde.getTime());
            query.append(" AND o.fechaInicio >= '").append(fDesde).append("' ");
        }
        if (fechaHasta != null) {
            Timestamp fHasta = new Timestamp(fechaHasta.getTime());
            query.append(" AND o.fechaFin <= '").append(fHasta).append("' ");
        }

        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public void create(Date fechaDesde, Date fechaHasta) throws Exception {
        try {
            Periodo unPeriodo = new Periodo();
            unPeriodo.setEstado(true);
            unPeriodo.setFechaInicio(fechaDesde);
            unPeriodo.setFechaFin(fechaHasta);

            this.create(unPeriodo);
        } catch (Exception ex) {
            throw new Exception("Error al intentar crear el preriodo: " + ex.getMessage());
        }
    }

    @Override
    public void edit(Long idPeriodo, Date fechaDesde, Date fechaHasta) throws Exception {
        try {

            Periodo unPeriodo = this.find(idPeriodo);
            unPeriodo.setEstado(true);
            unPeriodo.setFechaInicio(fechaDesde);
            unPeriodo.setFechaFin(fechaHasta);

            this.create(unPeriodo);

        } catch (Exception ex) {
            throw new Exception("Error al intentar editar el Periodo. " + ex.getMessage());
        }
    }

    @Override
    public void remove(Long idPeriodo) throws Exception {
        try {
            Periodo periodoAux = this.find(idPeriodo);
            periodoAux.setEstado(false);
            this.edit(periodoAux);
        } catch (Exception ex) {
            throw new Exception("Error al intentar borrar el Periodo.");
        }
    }

}
