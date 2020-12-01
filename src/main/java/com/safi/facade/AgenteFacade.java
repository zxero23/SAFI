/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Agente;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class AgenteFacade extends AbstractFacade<Agente> implements AgenteFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgenteFacade() {
        super(Agente.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Agente> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Agente as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Agente> findAll(String apellidoNombre) {
        return em.createQuery("select object(o) FROM Agente as o WHERE o.estado = true AND o.apellidoNombre LIKE '" + apellidoNombre + "%' ORDER BY o.id ").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Agente> findAllNotCB(boolean estado) {
        Query consulta = em.createQuery("select object(a) from Agente as a WHERE a.estado = :p1 AND a.id NOT IN (SELECT cb.representante.agente.id FROM CuentaBancaria cb WHERE cb.estado=true) ORDER BY a.id ASC");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Agente> findAll(Long dni, Integer legajo, String apellidoNombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Agente as o WHERE  o.estado = true");
        if (dni != null && dni != 0) {
            query.append(" and o.dni =").append(dni);
        }
        if (legajo != null && legajo != 0) {
            query.append(" and o.legajo =").append(legajo);
        }
        if (apellidoNombre != null && !apellidoNombre.equals("")) {
            query.append(" and o.apelidoNombre LIKE '%").append(apellidoNombre).append("%'");
        }
        query.append(" ORDER BY o.dni");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Agente> findAll(Long dni, Integer legajo, String apellidoNombre,int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Agente as o WHERE o.estado = true ");
         if (dni != null && dni != 0) {
            query.append(" and o.dni =").append(dni);
        }
        if (legajo != null && legajo != 0) {
            query.append(" and o.legajo =").append(legajo);
        }
        if (apellidoNombre != null && !apellidoNombre.equals("")) {
            query.append(" and o.apelidoNombre LIKE '%").append(apellidoNombre).append("%'");
        }
        query.append("ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long dni, Integer legajo, String apellidoNombre) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Agente as o WHERE o.estado = true ");
         if (dni != null && dni != 0) {
            query.append(" and o.dni =").append(dni);
        }
        if (legajo != null && legajo != 0) {
            query.append(" and o.legajo =").append(legajo);
        }
        if (apellidoNombre != null && !apellidoNombre.equals("")) {
            query.append(" and o.apelidoNombre LIKE '%").append(apellidoNombre).append("%'");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public void create(Long dni, Integer legajo, String apellidoNombre) throws Exception {
        try {
            Agente agenteAux = new Agente();
            agenteAux.setDni(dni);
            agenteAux.setApellidoNombre(apellidoNombre.toUpperCase());
            agenteAux.setLegajo(legajo);
            agenteAux.setEstado(true);
            this.create(agenteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el agente");
        }
    }

    @Override
    public void edit(Long idAgente, Long dni, Integer legajo, String apellidoNombre) throws Exception {
        try {
            Agente agenteAux = this.find(idAgente);
            agenteAux.setDni(dni);
            agenteAux.setApellidoNombre(apellidoNombre.toUpperCase());
            agenteAux.setLegajo(legajo);
            this.edit(agenteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el agente");
        }
    }

    @Override
    public void remove(Long idAgente) throws Exception {
        try {
            Agente agenteAux = this.find(idAgente);
            agenteAux.setEstado(false);
            this.edit(agenteAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el agente");
        }
    }

}
