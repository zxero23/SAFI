/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoClasificadorRecursoPropio;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface TipoClasificadorRecursoPropioFacadeLocal {

    public void create(TipoClasificadorRecursoPropio tipoClasificadorRecursoPropio);

    public void edit(TipoClasificadorRecursoPropio tipoClasificadorRecursoPropio);

    public void remove(TipoClasificadorRecursoPropio tipoClasificadorRecursoPropio);

    public TipoClasificadorRecursoPropio find(Object id);

    public List<TipoClasificadorRecursoPropio> findAll();

    public List<TipoClasificadorRecursoPropio> findRange(int[] range);

    public int count();

    public List<TipoClasificadorRecursoPropio> findAll(boolean estado);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoClasificador, String nombre) throws Exception;

    public void remove(Long idTipoClasificadorRecursoPropio) throws Exception;

    public List<TipoClasificadorRecursoPropio> findAll(String nombre);

    public List<TipoClasificadorRecursoPropio> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
