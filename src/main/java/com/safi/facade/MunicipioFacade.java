/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Departamento;
import com.safi.entity.Municipio;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Alvarenga Angel
 */
@Stateless
public class MunicipioFacade extends AbstractFacade<Municipio> implements MunicipioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @EJB
    DepartamentoFacadeLocal departamentoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MunicipioFacade() {
        super(Municipio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Municipio as o WHERE o.estado = :p1 ORDER BY o.departamento.provincia.nombre, o.departamento.nombre, o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAllByProvincia(Long idProvincia) {
        Query consulta = em.createQuery("select object(o) from Municipio as o WHERE o.estado = :p1 and o.departamento.provincia.id = :p2  order BY  o.nombre,o.departamento.provincia.nombre, o.departamento.nombre");
        consulta.setParameter("p1", true);
        consulta.setParameter("p2", idProvincia);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAllByDepto(Long idDepto) {
        Query consulta = em.createQuery("select object(o) from Municipio as o WHERE o.estado = :p1 and o.departamento.id = :p2 order BY o.departamento.provincia.nombre, o.departamento.nombre, o.nombre");
        consulta.setParameter("p1", true);
        consulta.setParameter("p2", idDepto);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(String nombre) {
        return em.createQuery("select object(o) FROM Municipio as o WHERE o.estado = true AND o.nombre LIKE '%" + nombre.toUpperCase() + "%' ORDER BY o.nombre").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(Long idDepartamento) {
        return em.createQuery("select object(o) FROM Municipio as o WHERE o.estado = true AND o.departamento.id= " + idDepartamento + " ORDER BY o.nombre").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(Long idProvincia,
            Long idDepartamento, String nombre, String codigoPostal) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Municipio as o WHERE  o.estado = true");
        if (idProvincia != null && idProvincia != 0L) {
            query.append(" and o.departamento.provincia.id =").append(idProvincia);
        }
        if (idDepartamento != null && idDepartamento != 0L) {
            query.append(" and o.departamento.id =").append(idDepartamento);
        }
        if (!(nombre.equals(""))) {
            query.append(" and o.nombre LIKE '%").append(nombre.toUpperCase()).append("%'");
        }
        if (!(codigoPostal.equals(""))) {
            query.append(" and o.codigoPostal LIKE '%").append(codigoPostal.toUpperCase()).append("%'");
        }
        query.append(" ORDER BY o.departamento.provincia.nombre, o.departamento.nombre, o.nombre");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(Long idProvincia,Long idDepartamento, String nombre, String codigoPostal, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Municipio as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        if (idProvincia != null && idProvincia != 0L) {
            query.append(" and o.departamento.provincia.id =").append(idProvincia);
        }
        if (idDepartamento != null && idDepartamento != 0L) {
            query.append(" and o.departamento.id =").append(idDepartamento);
        }
        if (!(codigoPostal.equals(""))) {
            query.append(" and o.codigoPostal LIKE '%").append(codigoPostal.toUpperCase()).append("%'");
        }
        query.append(" ORDER BY o.departamento.provincia.nombre, o.departamento.nombre, o.nombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idProvincia,Long idDepartamento, String nombre, String codigoPostal) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Municipio as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        if (idProvincia != null && idProvincia != 0L) {
            query.append(" and o.departamento.provincia.id =").append(idProvincia);
        }
        if (idDepartamento != null && idDepartamento != 0L) {
            query.append(" and o.departamento.id =").append(idDepartamento);
        }
        if (!(codigoPostal.equals(""))) {
            query.append(" and o.codigoPostal LIKE '%").append(codigoPostal.toUpperCase()).append("%'");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public Municipio find(String nombre, String nombreProvincia) {
        StringBuilder query = new StringBuilder();
        query.append("select object(m) FROM Municipio as m WHERE m.estado = true");
        query.append(" AND m.nombre = '").append(nombre.toUpperCase()).append("'");
        query.append(" AND m.departamento.provincia.nombre = '").append(nombreProvincia.toUpperCase()).append("'");
        Query consulta = em.createQuery(query.toString());
        return (Municipio) consulta.getSingleResult();
    }

    @Override
    public void create(Long idDepto, String codPostal, String nombre) throws Exception {
        try {

            if (!this.findAll(null, null, nombre, codPostal).isEmpty()) {
                throw new Exception("El municipio ya existe.");
            }

            Municipio municipioAux = new Municipio();
            Departamento deptoAux = this.departamentoFacade.find(idDepto);
            municipioAux.setCodigoPostal(codPostal);
            municipioAux.setNombre(nombre.toUpperCase());
            municipioAux.setEstado(true);
            this.create(municipioAux);
            deptoAux.getMuncipios().add(municipioAux);
            this.departamentoFacade.edit(deptoAux);
            municipioAux.setDepartamento(deptoAux);
            this.edit(municipioAux);
        } catch (Exception ex) {
            throw new Exception("Error al intentar crear el municipio: " + ex.getMessage());
        }
    }

    @Override
    public void edit(Long idMunicipio, String codPostal, String nombre) throws Exception {
        try {
            Municipio municipioAux = this.find(idMunicipio);
            municipioAux.setCodigoPostal(codPostal);
            municipioAux.setNombre(nombre.toUpperCase());
            this.edit(municipioAux);
        } catch (Exception ex) {
            throw new Exception("Error al intentar editar el municipio");
        }
    }

    @Override
    public void remove(Long idMunicipio) throws Exception {
        try {
            Municipio municipioAux = this.find(idMunicipio);
            municipioAux.setEstado(false);
            this.edit(municipioAux);
        } catch (Exception ex) {
            throw new Exception("Error al intentar borrar el municipio");
        }
    }
}
