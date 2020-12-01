/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Organismo;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface OrganismoFacadeLocal {

    public void create(Organismo organismo);

    public void edit(Organismo organismo);

    public void remove(Organismo organismo);

    public Organismo find(Object id);

    public List<Organismo> findAll();

    public List<Organismo> findRange(int[] range);

    public int count();

    public List<Organismo> findAll(boolean estado);
    
     public List<Organismo> findAll(Long idServicio, boolean estado) ;

    public List<Organismo> findAll(String nombre);

    public List<Organismo> findAll(Long codigoOrganismo, String nombre, 
            Long idOrganismoPadre, Long idClasificadorOrganismo);
    
    public Organismo findByCodigo(Long codigo);

    public void create(Long idOrganismoPadre, Long idClasificadorOrganismo, 
            Long codigoOrganismo, String nombre, String direccion, 
            String telefonos, String correoOficial) throws Exception;

    public void remove(Long idOrganismo) throws Exception;

    public void edit(Long id, Long idOrganismoPadre, Long idClasificadorOrganismo, 
            Long codigoOrganismo, String nombre,
            String direccion, String telefonos, 
            String correoOficial) throws Exception;    

    public List<Organismo> findAllNotServicio(boolean estado);

    public List<Organismo> findAll(List<Long> organismosId);

    public boolean existsOrganismo(Long codigoOrganismo);

    public List<Organismo> findAll(Long codigoOrganismo, String nombre, Long idOrganismoPadre, Long idClasificadorOrganismo, int first, int pageSize);

    public Long countAll(Long codigoOrganismo, String nombre, Long idOrganismoPadre, Long idClasificadorOrganismo);

}
