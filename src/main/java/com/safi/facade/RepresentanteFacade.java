/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Agente;
import com.safi.entity.Representante;
import com.safi.facade.AbstractFacade;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class RepresentanteFacade extends AbstractFacade<Representante> implements RepresentanteFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    AgenteFacadeLocal agenteFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RepresentanteFacade() {
        super(Representante.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Representante> findAll(List<Long> organismosId) {
        String iDes = organismosId.toString().substring(1, organismosId.toString().length() - 1);
        return em.createQuery("select object (o) from Representante as o where o.id in (" + iDes + ")").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Representante> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(r) from Representante as r WHERE r.estado = :p1  ORDER BY r.id ASC");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Representante> findAll(String nombre) {
        return em.createQuery("select object(o) FROM Representante as o INNER JOIN FETCH o.agente a WHERE  o.estado = true AND a.apellidoNombre LIKE '%" + nombre + "%' ORDER BY a.apellidoNombre ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Representante> findAll(String apellidoNombre, Long dni, Long idEstadoRepresentante) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Representante as o INNER JOIN FETCH o.agente a WHERE o.estado = true ");

        if (apellidoNombre != null && !apellidoNombre.equals("")) {
            query.append(" and a.apellidoNombre LIKE '%").append(apellidoNombre).append("%'");
        }
        if (dni != null) {
            query.append(" and o.dni =").append(dni);
        }
        if (idEstadoRepresentante != null && idEstadoRepresentante != 0L) {
            query.append(" and o.estadoRepresentante.id =").append(idEstadoRepresentante);
        }
        query.append(" ORDER BY o.id ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Representante> findAll(String apellidoNombre, Long dni, Long idEstadoRepresentante, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Representante as o WHERE o.estado = true ");
        if (apellidoNombre != null && !apellidoNombre.equals("")) {
            query.append(" and a.apellidoNombre LIKE '%").append(apellidoNombre).append("%'");
        }
        if (dni != null) {
            query.append(" and o.dni =").append(dni);
        }
        if (idEstadoRepresentante != null && idEstadoRepresentante != 0L) {
            query.append(" and o.estadoRepresentante.id =").append(idEstadoRepresentante);
        }
        query.append(" ORDER BY o.id ASC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(String apellidoNombre, Long dni, Long idEstadoRepresentante) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Representante as o WHERE o.estado = true ");
        if (apellidoNombre != null && !apellidoNombre.equals("")) {
            query.append(" and a.apellidoNombre LIKE '%").append(apellidoNombre).append("%'");
        }
        if (dni != null) {
            query.append(" and o.dni =").append(dni);
        }
        if (idEstadoRepresentante != null && idEstadoRepresentante != 0L) {
            query.append(" and o.estadoRepresentante.id =").append(idEstadoRepresentante);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public void create(Long idAgente, Date fechaInicio, Date fechaFin) throws Exception {
        try {
            Representante representanteAux = new Representante();
            Agente agenteAux = this.agenteFacade.find(idAgente);
            representanteAux.setAgente(agenteAux);
            representanteAux.setFechaInicio(fechaInicio);
            representanteAux.setFechaFin(fechaFin);
            representanteAux.setEstado(true);
            this.create(representanteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el representante.");
        }
    }

    @Override
    public void remove(Long idRepresentante) throws Exception {
        try {
            Representante organismoAux = this.find(idRepresentante);
            organismoAux.setEstado(false);
            this.edit(organismoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el representante.");
        }
    }

    @Override
    public void edit(Long id, Long idAgente, Date fechaInicio, Date fechaFin) throws Exception {
        try {
            Representante representanteAux = new Representante();
            Agente agenteAux = this.agenteFacade.find(idAgente);
            representanteAux.setAgente(agenteAux);
            representanteAux.setFechaInicio(fechaInicio);
            representanteAux.setFechaFin(fechaFin);
            this.edit(representanteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el representante.");
        }
    }
}
