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
import com.safi.entity.Provincia;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Alvarenga Angel
 */
@Stateless
public class DepartamentoFacade extends AbstractFacade<Departamento> implements DepartamentoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private ProvinciaFacadeLocal ProvinciaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartamentoFacade() {
        super(Departamento.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Departamento as o WHERE o.estado = :p1 ORDER BY o.provincia.nombre, o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> findAll(Long idProvincia, String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Departamento as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        if (idProvincia != null && !idProvincia.equals(0L)) {
            query.append(" AND o.provincia.id=").append(idProvincia);
        }
        query.append(" ORDER BY o.provincia.nombre,o.nombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idProvincia, String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Departamento as o WHERE o.estado = true ");
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        if (idProvincia != null && !idProvincia.equals(0L)) {
            query.append(" AND o.provincia.id=").append(idProvincia);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> findAllByProvincia(Long idProvincia) {
        Query consulta = em.createQuery("select object(o) from Departamento as o WHERE o.estado = :p1 and o.provincia.id = :p2 ORDER BY o.provincia.nombre, o.nombre");
        consulta.setParameter("p1", true);
        consulta.setParameter("p2", idProvincia);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> findAllByZona(Long idZona) {
        Query consulta = em.createQuery("select object(o) from Departamento as o WHERE o.estado = :p1 and o.zona.id = :p2 ORDER BY o.zona.nombre, o.nombre");
        consulta.setParameter("p1", true);
        consulta.setParameter("p2", idZona);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> findAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Departamento as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        query.append("ORDER BY o.nombre");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> finDepartamentos(List<Long> deptosId) {
        String iDes = deptosId.toString().substring(1, deptosId.toString().length() - 1);
        return em.createQuery("select object (g) from Departamento as g where g.id in (" + iDes + " ) ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Departamento> findAll(Long idProvincia, String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Departamento as o WHERE  o.estado = true");

        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }

        if (idProvincia != null && idProvincia != 0L) {
            query.append(" and o.provincia.id =").append(idProvincia);
        }
        query.append(" ORDER BY o.provincia.nombre, o.nombre");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @Override
    public void create(Long idProvincia, String nombre) throws Exception {
        try {
            boolean isNameEqual = false;
            boolean isProvEqual = false;
            List<Departamento> listDepartamento = this.findAll(true);
            for (Departamento departamento : listDepartamento) {
                if (departamento.getNombre().equalsIgnoreCase(nombre)) {
                    isNameEqual = true;
                    if (departamento.getProvincia().getId().equals(idProvincia)) {
                        isProvEqual = true;
                        break;
                    }
                }
            }
            if (isNameEqual && isProvEqual) {
                throw new Exception("El Departamento ya existe");
            }
            Provincia provinciaAux = this.ProvinciaFacade.find(idProvincia);
            Departamento deptoAux = new Departamento();
            deptoAux.setNombre(nombre.toUpperCase());
            deptoAux.setProvincia(provinciaAux);
            deptoAux.setEstado(true);
            this.create(deptoAux);
            provinciaAux.getDepartamentos().add(deptoAux);
            this.ProvinciaFacade.edit(provinciaAux);
        } catch (Exception ex) {
            throw new Exception("Error al intentar crear el departamento: " + ex.getMessage());
        }
    }

    @Override
    public void edit(Long id, String nombre) throws Exception {
        try {
            Departamento deptoAux = this.find(id);
            deptoAux.setNombre(nombre.toUpperCase());
            this.edit(deptoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el departamento");
        }
    }

    @Override
    public void remove(Long id) throws Exception {
        try {
            Departamento deptoAux = this.find(id);
            Provincia provinicaAux = deptoAux.getProvincia();
            deptoAux.setEstado(false);
            this.edit(deptoAux);
            provinicaAux.getDepartamentos().remove(deptoAux);
            this.ProvinciaFacade.edit(provinicaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el departamento");
        }
    }

}
