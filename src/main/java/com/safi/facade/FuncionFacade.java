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
import com.safi.entity.Funcion;
import com.safi.entity.TipoAcumulador;
import com.safi.entity.TipoFondo;
import com.safi.entity.TipoImputacion;
import com.safi.entity.TipoOrdenGasto;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class FuncionFacade extends AbstractFacade<Funcion> implements FuncionFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private TipoOrdenGastoFacadeLocal tipoOrdenGastoFacade;
    @EJB
    private TipoFondoFacadeLocal tipoFondoFacade;
    @EJB
    private TipoImputacionFacadeLocal tipoImputacionFacade;
    @EJB
    private TipoAcumuladorFacadeLocal tipoAcumuladorFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FuncionFacade() {
        super(Funcion.class);
    }

    /**
     * @author Alvarenga Angel
     * @param estado
     * @return List Funcion
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Funcion> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Funcion as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }


    /**
     * @author Alvarenga Angel
     * @param idTipoOrdenGasto
     * @param idTipoFondo
     * @param idTipoImputacion
     * @param idTipoAcumulador
     * @param tipoOperacion
     * @return Funcion
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Funcion find(Long idTipoOrdenGasto, Long idTipoFondo,
            Long idTipoImputacion, Long idTipoAcumulador, int tipoOperacion) {
        Funcion funcionAux;
        try {
            Query consulta = em.createQuery("select object(o) from Funcion as o WHERE o.tipoOrdenGasto.id = :p1 and o.tipoFondo.id = :p2 and o.tipoImputacion.id = :p3 and o.tipoAcumulador.id = :p4 and o.tipoOperacion = :p5 and o.estado = :p6");
            consulta.setParameter("p1", idTipoOrdenGasto);
            consulta.setParameter("p2", idTipoFondo);
            consulta.setParameter("p3", idTipoImputacion);
            consulta.setParameter("p4", idTipoAcumulador);
            consulta.setParameter("p5", tipoOperacion);
            consulta.setParameter("p6", true);
            funcionAux = (Funcion) consulta.getSingleResult();
        } catch (Exception e) {
            funcionAux = null;
        }
        return funcionAux;
    }

    /**
     * @author Alvarenga Angel
     * @param idTipoOrdenGasto
     * @param idTipoFondo
     * @param idTipoImputacion
     * @param idTipoAcumulador
     * @return List Funcion
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Funcion> findAll(Long idTipoOrdenGasto, Long idTipoFondo,
            Long idTipoImputacion, Long idTipoAcumulador) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Funcion as o WHERE  o.estado = true");
        if (idTipoOrdenGasto != null && idTipoOrdenGasto != 0L) {
            query.append(" and o.tipoOrdenGasto.id =").append(idTipoOrdenGasto);
        }
        if (idTipoFondo != null && idTipoFondo != 0L) {
            query.append(" and o.tipoFondo.id =").append(idTipoFondo);
        }
        if (idTipoImputacion != null && idTipoImputacion != 0L) {
            query.append(" and o.tipoImputacion.id =").append(idTipoImputacion);
        }
        if (idTipoAcumulador != null && idTipoAcumulador != 0L) {
            query.append(" and o.tipoAcumulador.id =").append(idTipoAcumulador);
        }
        query.append(" ORDER BY o.id");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Funcion> findAll(Long idTipoOrdenGasto, Long idTipoFondo,
            Long idTipoImputacion, Long idTipoAcumulador, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Funcion as o WHERE o.estado = true ");
        if (idTipoOrdenGasto != null && idTipoOrdenGasto != 0L) {
            query.append(" and o.tipoOrdenGasto.id =").append(idTipoOrdenGasto);
        }
        if (idTipoFondo != null && idTipoFondo != 0L) {
            query.append(" and o.tipoFondo.id =").append(idTipoFondo);
        }
        if (idTipoImputacion != null && idTipoImputacion != 0L) {
            query.append(" and o.tipoImputacion.id =").append(idTipoImputacion);
        }
        if (idTipoAcumulador != null && idTipoAcumulador != 0L) {
            query.append(" and o.tipoAcumulador.id =").append(idTipoAcumulador);
        }
        query.append(" ORDER BY o.id");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idTipoOrdenGasto, Long idTipoFondo,
            Long idTipoImputacion, Long idTipoAcumulador) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Funcion as o WHERE o.estado = true ");
       if (idTipoOrdenGasto != null && idTipoOrdenGasto != 0L) {
            query.append(" and o.tipoOrdenGasto.id =").append(idTipoOrdenGasto);
        }
        if (idTipoFondo != null && idTipoFondo != 0L) {
            query.append(" and o.tipoFondo.id =").append(idTipoFondo);
        }
        if (idTipoImputacion != null && idTipoImputacion != 0L) {
            query.append(" and o.tipoImputacion.id =").append(idTipoImputacion);
        }
        if (idTipoAcumulador != null && idTipoAcumulador != 0L) {
            query.append(" and o.tipoAcumulador.id =").append(idTipoAcumulador);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    /**
     * @author Alvarenga Angel
     * @param idTipoOrdenGasto
     * @param idTipoFondo
     * @param idTipoImputacion
     * @param idTipoAcumulador
     * @param tipoOperacion
     * @throws Exception
     */
    @Override
    public void create(Long idTipoOrdenGasto, Long idTipoFondo, Long idTipoImputacion,
            Long idTipoAcumulador, int tipoOperacion) throws Exception {
        try {
            Funcion funcionReturn = this.find(idTipoOrdenGasto, idTipoFondo, idTipoImputacion, idTipoAcumulador, tipoOperacion);
            if (funcionReturn == null) {
                TipoOrdenGasto tipoOrdenGastoAux = this.tipoOrdenGastoFacade.find(idTipoOrdenGasto);
                TipoFondo tipoFondoAux = this.tipoFondoFacade.find(idTipoFondo);
                TipoImputacion tipoImputacionAux = this.tipoImputacionFacade.find(idTipoImputacion);
                TipoAcumulador tipoAcumuladorAux = this.tipoAcumuladorFacade.find(idTipoAcumulador);
                Funcion funcionAux = new Funcion();
                funcionAux.setTipoOrdenGasto(tipoOrdenGastoAux);
                funcionAux.setTipoFondo(tipoFondoAux);
                funcionAux.setTipoAcumulador(tipoAcumuladorAux);
                funcionAux.setTipoImputacion(tipoImputacionAux);
                funcionAux.setTipoOperacion(tipoOperacion);
                funcionAux.setEstado(true);
                this.create(funcionAux);
            } else {
                throw new Exception("La función ya se encuentra registrada.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la función. " + e.getMessage());
        }
    }

    /**
     * @author Alvarenga Angel
     * @param idFuncion
     * @param idTipoOrdenGasto
     * @param idTipoFondo
     * @param idTipoImputacion
     * @param idTipoAcumulador
     * @param tipoOperacion
     * @throws Exception
     */
    @Override
    public void edit(Long idFuncion, Long idTipoOrdenGasto, Long idTipoFondo,
            Long idTipoImputacion, Long idTipoAcumulador, int tipoOperacion) throws Exception {
        try {
            Funcion funcionReturn = this.find(idTipoOrdenGasto, idTipoFondo, idTipoImputacion, idTipoAcumulador, tipoOperacion);
            if (funcionReturn == null) {
                TipoOrdenGasto tipoOrdenGastoAux = this.tipoOrdenGastoFacade.find(idTipoOrdenGasto);
                TipoFondo tipoFondoAux = this.tipoFondoFacade.find(idTipoFondo);
                TipoImputacion tipoImputacionAux = this.tipoImputacionFacade.find(idTipoImputacion);
                TipoAcumulador tipoAcumuladorAux = this.tipoAcumuladorFacade.find(idTipoAcumulador);
                Funcion funcionAux = this.find(idFuncion);
                funcionAux.setTipoOrdenGasto(tipoOrdenGastoAux);
                funcionAux.setTipoFondo(tipoFondoAux);
                funcionAux.setTipoAcumulador(tipoAcumuladorAux);
                funcionAux.setTipoImputacion(tipoImputacionAux);
                funcionAux.setTipoOperacion(tipoOperacion);
                this.edit(funcionAux);
            } else {
                throw new Exception("La función ya se encuentra registrada.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la función. " + e.getMessage());
        }
    }

    /**
     * @author Alvarenga Angel
     * @param idFuncion
     * @throws Exception
     */
    @Override
    public void remove(Long idFuncion) throws Exception {
        try {
            Funcion funcionAux = this.find(idFuncion);
            funcionAux.setEstado(false);
            this.edit(funcionAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la función");
        }
    }

}
