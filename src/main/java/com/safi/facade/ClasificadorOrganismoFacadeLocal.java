/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.ClasificadorOrganismo;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface ClasificadorOrganismoFacadeLocal {

    public void create(ClasificadorOrganismo clasificadorOrganismo);

    public void edit(ClasificadorOrganismo clasificadorOrganismo);

    public void remove(ClasificadorOrganismo clasificadorOrganismo);

    public ClasificadorOrganismo find(Object id);

    public List<ClasificadorOrganismo> findAll();

    public List<ClasificadorOrganismo> findRange(int[] range);

    public int count();

    public List<ClasificadorOrganismo> findAll(boolean estado);

    public List<ClasificadorOrganismo> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idClasificadorOrganismo, String nombre) throws Exception;

    public void remove(Long idClasificadorOrganismo) throws Exception;

    public List<ClasificadorOrganismo> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
