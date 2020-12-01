/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.CodigoExpediente;
import com.safi.entity.Servicio;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface ServicioFacadeLocal {

    public void create(Servicio servicio);

    public void edit(Servicio servicio);

    public void remove(Servicio servicio);

    public Servicio find(Object id);

    public List<Servicio> findAll();

    public List<Servicio> findRange(int[] range);

    public int count();

    public List<Servicio> findAll(boolean estado);
    
    public List<Servicio> findAll(Long idEjercicio);
    
    public List<Servicio> findAll(Long id,String codigo, String abreviatura, String descripcion);

    public void create(String codigo,String abreviatura, String descripcion, Long idTipoServicio,List<CodigoExpediente> lstCodigoExpediente) throws Exception;

    public void remove(Long idServicio) throws Exception;

    public void edit(Long idServicio,String codigo, String abreviatura, String descripcion, Long idTipoServicio,List<Long> lstCodigoExpediente) throws Exception;

    public void editUsuariosServicio(Long id, List<Long> lstUsuariosIzquierda,List<Long> lstUsuariosDerecha) throws Exception;

    public void editOrganismosServicio(Long id, List<Long> lstOrganismos) throws Exception;    

    public boolean existsServicio(String codigo);
    
    public List<Servicio> findAll(Long idServicio,Long idEjercicio) ;

    public List<Servicio> findAll(Long id, String codigo, String abreviatura, String descripcion, Long idTipoServicio, int first, int pageSize);

    public Long countAll(Long id, String codigo, String abreviatura, String descripcion, Long idTipoServicio);
    
    public List<Servicio> findAllServicio(Long idServicio,Long idEjercicio) ;
    
    
}
