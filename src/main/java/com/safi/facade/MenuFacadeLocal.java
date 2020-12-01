/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Menu;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface MenuFacadeLocal {

    void create(Menu menu);

    void edit(Menu menu);

    void remove(Menu menu);

    Menu find(Object id);

    List<Menu> findAll();

    List<Menu> findRange(int[] range);

    int count();
    
    public List<Menu> findAll(boolean estado);

    public List<Menu> findAll(String nombreMenu);

    public List<Menu> findAll(List<Long> menuId);

    public List<Menu> findAll(Long id);

    public List<Menu> findAllMenu();

    public void create(Long idMenuPadre, String nombre, String link, String icon) throws Exception;

    public void remove(Long idMenu) throws Exception;

    public void edit(Long id, Long idSeleccion, String nombre, String link, String icon) throws Exception;

    public List<Menu> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
