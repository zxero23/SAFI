/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.ActionItem;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface ActionItemFacadeLocal {

    void create(ActionItem actionItem);

    void edit(ActionItem actionItem);

    void remove(ActionItem actionItem);

    ActionItem find(Object id);

    List<ActionItem> findAll();

    List<ActionItem> findRange(int[] range);

    int count();
    
    public List<ActionItem> findAll(String nombreAccion, int first, int pageSize);

    public List<ActionItem> findAll(List<Long> actionsId);

    public void create(String nombre) throws Exception;

    public void remove(Long idAccion) throws Exception;

    public void edit(Long idAccion, String nombreAccion) throws Exception;

    public Long countAll(String nombre);

    public List<ActionItem> findAll(boolean estado);
}
