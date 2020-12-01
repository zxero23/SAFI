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
import com.safi.entity.Usuario;
import com.safi.entity.CierreEjercicio;
import com.safi.entity.Ejercicio;
import com.safi.entity.Servicio;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class CierreEjercicioFacade extends AbstractFacade<CierreEjercicio> implements CierreEjercicioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    UsuarioFacadeLocal usuarioFacade;
    @EJB
    EjercicioFacadeLocal ejercicioFacade;
    @EJB
    ServicioFacadeLocal servicioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CierreEjercicioFacade() {
        super(CierreEjercicio.class);
    }

    /**
     * @author Zakowicz Matias
     * @param estado
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CierreEjercicio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from CierreEjercicio as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    /**
     * @author Zakowicz Matias
     * @param idEjercicio
     * @param idServicio
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CierreEjercicio> findAll(Long idEjercicio, Long idServicio) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM CierreEjercicio as o WHERE  o.estado = true");
        if (idEjercicio != null && idEjercicio != 0) {
            query.append(" and o.ejercicio.id =").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id =").append(idServicio);
        }
        query.append(" ORDER BY o.id DESC");
        
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    /**
     * @author Zakowicz Matias
     * @param idEjercicio
     * @param idServicio
     * @param idUsuario
     * @throws Exception
     */
    @Override
    public void create(Long idEjercicio, Long idServicio, Long idUsuario) throws Exception {
        try {
            Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
            Servicio servicioAux = this.servicioFacade.find(idServicio);
            Usuario usuarioAux = this.usuarioFacade.find(idUsuario);
            CierreEjercicio cierreEjercicioAux = new CierreEjercicio();
            cierreEjercicioAux.setEjercicio(ejercicioAux);
            cierreEjercicioAux.setServicio(servicioAux);
            cierreEjercicioAux.setUsuario(usuarioAux);
            cierreEjercicioAux.setFechaCierre(new Date());
            cierreEjercicioAux.setEstado(true);
            this.create(cierreEjercicioAux);
            ejercicioAux.setCierreEjercicio(cierreEjercicioAux);
            this.ejercicioFacade.edit(ejercicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el cierre del ejercicio.");
        }
    }

    /**
     * @author Zakowicz Matias
     * @param idCierreEjercicio
     * @param idEjercicio
     * @param idServicio
     * @param idUsuario
     *
     * @throws Exception
     */
    @Override
    public void edit(Long idCierreEjercicio, Long idEjercicio, Long idServicio, Long idUsuario) throws Exception {
        try {
            CierreEjercicio cierreEjercicioAux = this.find(idCierreEjercicio);
            Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
            Servicio servicioAux = this.servicioFacade.find(idServicio);
            Usuario usuarioAux = this.usuarioFacade.find(idUsuario);
            cierreEjercicioAux.setEjercicio(ejercicioAux);
            cierreEjercicioAux.setServicio(servicioAux);
            cierreEjercicioAux.setUsuario(usuarioAux);
            cierreEjercicioAux.setFechaCierre(new Date());
            this.edit(cierreEjercicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el cierre del ejercicio.");
        }
    }

    /**
     * @author Zakowicz Matias
     * @param idCierreEjercicio
     * @throws Exception
     */
    @Override
    public void remove(Long idCierreEjercicio) throws Exception {
        try {
            CierreEjercicio cierreEjercicioAux = this.find(idCierreEjercicio);
            cierreEjercicioAux.setEstado(false);
            this.edit(cierreEjercicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el cierre del ejercicio.");
        }
    }

}
