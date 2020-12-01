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
import com.safi.entity.Menu;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class MenuFacade extends AbstractFacade<Menu> implements MenuFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MenuFacade() {
        super(Menu.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Menu> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Menu as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Menu> findAll(String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Menu as o WHERE o.estado = true ");
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
        query.append("select COUNT(o) FROM Menu as o WHERE o.estado = true ");
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
    public List<Menu> findAll(String nombreMenu) {
        return em.createQuery("select object(o) FROM Menu as o WHERE o.estado = true AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%" + Utilidad.desacentuar(nombreMenu).toUpperCase() + "%' ORDER BY o.nombre ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Menu> findAll(List<Long> menuId) {
        String iDes = menuId.toString().substring(1, menuId.toString().length() - 1);
        return em.createQuery("select object (m) from Menu as m where m.id in (" + iDes + ")").getResultList();
    }

    @Override
    public List<Menu> findAll(Long id) {
        Query jpqlString = em.createQuery("SELECT OBJECT (subm) FROM Menu AS m, IN (m.menus) subm where m.id = :p1 ORDER BY subm.orden");
        jpqlString.setParameter("p1", id);
        return jpqlString.getResultList();
    }

    @Override
    public List<Menu> findAllMenu() {
        Query consulta = em.createQuery("select object(o) from Menu as o WHERE o.estado = :p1 and o.menu is null");
        consulta.setParameter("p1", true);
        return consulta.getResultList();
    }

    @Override
    public void create(Long idMenuPadre, String nombre, String link, String icon) throws Exception {
        try {
            Menu menuAux = new Menu();
            menuAux.setNombre(nombre.toUpperCase());
            menuAux.setEstado(true);
            menuAux.setLink(link);
            if (icon.isEmpty() || icon == "") {
                menuAux.setIcon("fa fa-circle-o");
            } else {
                menuAux.setIcon(icon);
            }
            em.persist(menuAux);
            if (idMenuPadre > 0) {
                Menu menuPadreAux = this.find(idMenuPadre);
                menuPadreAux.getMenus().add(menuAux);
                menuAux.setMenu(menuPadreAux);
                this.edit(menuAux);
                this.edit(menuPadreAux);
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el menu.");
        }
    }

    @Override
    public void remove(Long idMenu) throws Exception {
        try {

            Menu menuAux = this.find(idMenu);
            menuAux.setEstado(false);
            Menu menuPadre = menuAux.getMenu();
            menuAux.setMenu(null);
            menuPadre.getMenus().remove(menuAux);
            em.merge(menuAux);
            em.merge(menuPadre);
        } catch (Exception e) {
            throw new Exception("Error al intetar borrar el menu.");
        }
    }

    @Override
    public void edit(Long id, Long idSeleccion, String nombre, String link, String icon) throws Exception {
        try {
            Menu menuPadreSeleccion = this.find(idSeleccion);
            Menu menuAux = this.find(id);
            Menu menuPadreActual = menuAux.getMenu();
            menuAux.setNombre(nombre.toUpperCase());
            menuAux.setEstado(true);
            menuAux.setLink(link);
            if (icon.isEmpty() || icon == "") {
                menuAux.setIcon("fa fa-circle-o");
            } else {
                menuAux.setIcon(icon);
            }
            if (menuPadreActual != null) {
                if (!(menuPadreActual.equals(menuPadreSeleccion))) {
                    if (menuPadreSeleccion != null) {
                        menuPadreActual.getMenus().remove(menuAux);
                        this.edit(menuPadreActual);
                        menuPadreSeleccion.getMenus().add(menuAux);
                        this.edit(menuPadreSeleccion);
                        menuAux.setMenu(menuPadreSeleccion);
                    } else {
                        menuPadreActual.getMenus().remove(menuAux);
                        this.edit(menuPadreActual);
                        menuAux.setMenu(menuPadreSeleccion);
                    }
                }
            } else {
                if (menuPadreSeleccion != null) {
                    menuPadreSeleccion.getMenus().add(menuAux);
                    this.edit(menuPadreSeleccion);
                    menuAux.setMenu(menuPadreSeleccion);
                }
            }
            this.edit(menuAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el menu.");
        }
    }

}
