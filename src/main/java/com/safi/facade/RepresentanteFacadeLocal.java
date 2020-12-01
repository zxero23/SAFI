/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Representante;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface RepresentanteFacadeLocal {

    void create(Representante representante);

    void edit(Representante representante);

    void remove(Representante representante);

    Representante find(Object id);

    List<Representante> findAll();

    List<Representante> findRange(int[] range);

    int count();

    public List<Representante> findAll(List<Long> organismosId);

    public List<Representante> findAll(boolean estado);

    public List<Representante> findAll(String nombre);

    public List<Representante> findAll(String apellidoNombre, Long dni, Long idEstadoRepresentante);

    public void create(Long idAgente, Date fechaInicio, Date fechaFin) throws Exception;

    public void remove(Long idRepresentante) throws Exception;

    public void edit(Long id, Long idAgente, Date fechaInicio, Date fechaFin) throws Exception;

    public List<Representante> findAll(String apellidoNombre, Long dni, Long idEstadoRepresentante, int first, int pageSize);

    public Long countAll(String apellidoNombre, Long dni, Long idEstadoRepresentante);

    
}
