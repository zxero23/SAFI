/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Departamento;

/**
 *
 * @author Alvarenga Angel
 */
@Local
public interface DepartamentoFacadeLocal {

    public void create(Departamento departamento);

    public void edit(Departamento departamento);

    public void remove(Departamento departamento);

    public Departamento find(Object id);

    public List<Departamento> findAll();

    public List<Departamento> findRange(int[] range);

    public int count();

    public List<Departamento> findAll(String leyenda);


    public void edit(Long id, String nombre) throws Exception;

    public void remove(Long id) throws Exception;

    public List<Departamento> findAll(boolean estado);

    public List<Departamento> finDepartamentos(List<Long> deptosId);

    public List<Departamento> findAllByProvincia(Long idProvincia);

    public void create(Long idProvincia, String nombre) throws Exception;

    public List<Departamento> findAll(Long idProvincia, String nombre);

    public List<Departamento> findAllByZona(Long idZona);

    public List<Departamento> findAll(Long idProvincia,String nombre, int first, int pageSize);

    public Long countAll(Long idProvincia,String nombre);

    
    
}
