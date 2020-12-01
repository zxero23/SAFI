/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.ClasificadorRecursoPropio;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface ClasificadorRecursoPropioFacadeLocal {

    public void create(ClasificadorRecursoPropio clasificadorRecursoPropio);

    public void edit(ClasificadorRecursoPropio clasificadorRecursoPropio);

    public void remove(ClasificadorRecursoPropio clasificadorRecursoPropio);

    public ClasificadorRecursoPropio find(Object id);

    public List<ClasificadorRecursoPropio> findAll();

    public List<ClasificadorRecursoPropio> findRange(int[] range);

    public int count();

    public List<ClasificadorRecursoPropio> findAll(boolean estado);

    public List<ClasificadorRecursoPropio> findAll(String nombre);

    public ClasificadorRecursoPropio create(String nombre, Long idClasificadorPadre, Long idTipoClasificadorRecursoPropio) throws Exception;

    public void edit(Long idClasificadorRecursoPropio, String nombre, Long idClasificadorPadre, Long idTipoClasificadorRecursoPropio) throws Exception;

    public void remove(Long idClasificadorRecursoPropio) throws Exception;

    public List<ClasificadorRecursoPropio> findAll(Long idClasificiadorPropio, Long codigo, Long idTipoClasificadorRecursoPropio, String nombre);

    public boolean exists(Long idClasificiadorPropio, Long codigo, Long idTipoClasificadorRecursoPropio);
    
    public List<String> findAllSelectItems(Long ejercicio_id, Long servicio_id) ;
    
    public List<String> findAllSelectItems(Long ejercicio_id, Long servicio_id,String sector) ;

    public List<ClasificadorRecursoPropio> findAll(Long idClasificiadorPropio, Long codigo, Long idTipoClasificadorRecursoPropio, String nombre, int first, int pageSize);

    public Long countAll(Long idClasificiadorPropio, Long codigo, Long idTipoClasificadorRecursoPropio, String nombre);
    
}
