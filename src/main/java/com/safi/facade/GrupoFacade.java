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
import com.safi.entity.ActionItem;
import com.safi.entity.Grupo;
import com.safi.entity.Menu;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class GrupoFacade extends AbstractFacade<Grupo> implements GrupoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private MenuFacadeLocal menuFacade;
    @EJB
    private ActionItemFacadeLocal actionFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GrupoFacade() {
        super(Grupo.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Grupo> finGrupos(List<Long> gruposId) {
        String iDes = gruposId.toString().substring(1, gruposId.toString().length() - 1);
        return em.createQuery("select object (g) from Grupo as g where g.id in (" + iDes + " ) ").getResultList();
    }

    @Override
    public List<Menu> findAllMenu(Long idGrupo) {
        Query jpqlString = em.createQuery("SELECT OBJECT (m) FROM Grupo AS g, IN (g.menus) m where g.id = :p1  and m.estado=:p2 ORDER BY m.orden");
        jpqlString.setParameter("p1", idGrupo);
        jpqlString.setParameter("p2", true);
        return jpqlString.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Grupo> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Grupo as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Grupo> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Grupo as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM Grupo as o WHERE o.estado = true ");
        if (nombre != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre).toUpperCase());
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Grupo> findAll(String nombreGrupo) {
        return em.createQuery("select object(o) FROM Grupo as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombreGrupo).toUpperCase() + "%' ORDER BY o.nombre ").getResultList();
    }

    @Override
    public void remove(Long idGrupo) throws Exception {
        try {
            Grupo grupo = this.find(idGrupo);
            grupo.setEstado(false);
            this.edit(grupo);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el grupo");
        }
    }

    @Override
    public void remove(Long idGrupo, List<Long> idAcciones) throws Exception {
        try {
            Grupo grupo = this.find(idGrupo);
            if (!(idAcciones.isEmpty())) {
                List<ActionItem> listAction = actionFacade.findAll(idAcciones);
                grupo.setAcciones(null);
                this.edit(grupo);
                grupo.setAcciones(listAction);
                this.edit(grupo);
            } else {
                grupo.setAcciones(null);
                this.edit(grupo);
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar acciones del grupo");
        }
    }

    @Override
    public void create(String nombreGrupo, String descripcionGrupo) throws Exception {
        try {
            Grupo grupo = new Grupo();
            grupo.setNombre(nombreGrupo.toUpperCase());
            grupo.setDescripcion(descripcionGrupo);
            grupo.setEstado(true);
            this.create(grupo);
        } catch (Exception ex) {
            throw new Exception("Error al intentar crear el grupo");
        }
    }

    @Override
    public void edit(Long idGrupo, String nombreGrupo, String descripcionGrupo) throws Exception {
        try {
            Grupo grupo = this.find(idGrupo);
            grupo.setNombre(nombreGrupo.toUpperCase());
            grupo.setDescripcion(descripcionGrupo);
            grupo.setEstado(true);
            this.edit(grupo);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el grupo");
        }
    }

    @Override
    public void edit(Long idGrupo, List<Long> idMenu) throws Exception {
        try {
            Grupo grupo = this.find(idGrupo);
            if (!(idMenu.isEmpty())) {
                List<Menu> listaMenu = menuFacade.findAll(idMenu);
                grupo.setMenus(null);
                grupo.setMenus(listaMenu);
            } else {
                grupo.setAcciones(null);
                grupo.setMenus(null);
            }
            this.edit(grupo);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el grupo");
        }
    }
    
    @Override
    public List<Menu> getMenus(Long idGrupo){
         Query consulta = em.createQuery("SELECT OBJECT (m) FROM Grupo AS g, IN (g.menus) m where m.estado = true and g.id = :p1 ");
         consulta.setParameter("p1", idGrupo);
         return consulta.getResultList();
    }
    
    @Override
    public List<ActionItem> getAcciones(Long idGrupo){
         Query consulta = em.createQuery("SELECT OBJECT (a) FROM Grupo AS g, IN (g.acciones) a where a.estado = true and g.id = :p1 ");
         consulta.setParameter("p1", idGrupo);
         return consulta.getResultList();
    }
   

}
