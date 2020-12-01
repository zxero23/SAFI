/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safi.facade;

import java.util.List;
import javax.ejb.Local;
import org.safi.entity.ActionItem;
import org.safi.entity.Grupo;
import org.safi.entity.Menu;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface GrupoFacadeLocal {

    void create(Grupo grupo);

    void edit(Grupo grupo);

    void remove(Grupo grupo);

    Grupo find(Object id);

    List<Grupo> findAll();

    List<Grupo> findRange(int[] range);

    int count();
    
    public void edit(Long idGrupo, List<Long> idMenu) throws Exception;

    public void edit(Long idGrupo, String nombreGrupo, String descripcionGrupo) throws Exception;

    public void create(String nombreGrupo, String descripcionGrupo) throws Exception;

    public void remove(Long idGrupo, List<Long> idAcciones) throws Exception;

    public void remove(Long idGrupo) throws Exception;

    public List<Grupo> findAll(String nombreGrupo);

    public List<Grupo> findAll(boolean estado);

    public List<Menu> findAllMenu(Long idGrupo);

    public List<Grupo> finGrupos(List<Long> gruposId);

    public List<Grupo> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);

    public List<Menu> getMenus(Long idGrupo);

    public List<ActionItem> getAcciones(Long idGrupo);
    
}
