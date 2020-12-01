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
import com.safi.entity.Banco;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@Stateless
public class BancoFacade extends AbstractFacade<Banco> implements BancoFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BancoFacade() {
        super(Banco.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Banco> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Banco as o WHERE o.estado = :p1 ORDER BY o.nombre ASC");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Banco> findAll(String nombre) {
        return em.createQuery("select object(o) FROM Banco as o WHERE o.estado = true AND o.nombre LIKE '%" + nombre + "%'").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Banco> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Banco as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        query.append("ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Banco as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }
    
    @Override
    public void create(String nombre) throws Exception {
        try {
            Banco bancoAux = new Banco();
            bancoAux.setNombre(nombre.toUpperCase());
            bancoAux.setEstado(true);
            this.create(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el banco");
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Banco> findByName(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Banco as o WHERE o.estado = true");
        if (!(nombre.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '").append(Utilidad.desacentuar(nombre).toUpperCase()).append("'");
        }
        query.append(" ORDER BY o.nombre ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
    @Override
    public void edit(Long idBanco, String nombre) throws Exception {
        try {
            Banco bancoAux = this.find(idBanco);
            bancoAux.setNombre(nombre.toUpperCase());
            this.edit(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el banco");
        }
    }
    
    @Override
    public void remove(Long idBanco) throws Exception {
        try {
            Banco bancoAux = this.find(idBanco);
            bancoAux.setEstado(false);
            this.edit(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el banco");
        }
    }
}

