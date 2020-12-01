/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.TipoAcumulador;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface TipoAcumuladorFacadeLocal {

    public void create(TipoAcumulador tipoAcumulador);

    public void edit(TipoAcumulador tipoAcumulador);

    public void remove(TipoAcumulador tipoAcumulador);

    public TipoAcumulador find(Object id);

    public List<TipoAcumulador> findAll();

    public List<TipoAcumulador> findRange(int[] range);

    public int count();    

    public List<TipoAcumulador> findAll(boolean estado);

    public List<TipoAcumulador> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void edit(Long idTipoAcumulador, String nombre) throws Exception;

    public void remove(Long idTipoAcumulador) throws Exception;

    public List<TipoAcumulador> findAll(String nombre, int first, int pageSize);

    public Long countAll(String nombre);
    
}
