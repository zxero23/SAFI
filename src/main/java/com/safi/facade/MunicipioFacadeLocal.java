/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Municipio;

/**
 *
 * @author Alvarenga Angel
 */
@Local
public interface MunicipioFacadeLocal {

    public void create(Municipio municipio);

    public void edit(Municipio municipio);

    public void remove(Municipio municipio);

    public Municipio find(Object id);

    public List<Municipio> findAll();

    public List<Municipio> findRange(int[] range);

    public int count();

    public List<Municipio> findAll(boolean estado);

    public List<Municipio> findAll(String nombre);

    public void create(Long idDepto, String codPostal, String nombre) throws Exception;

    public void edit(Long idMunicipio, String codPostal, String nombre) throws Exception;

    public void remove(Long idMunicipio) throws Exception;

    public List<Municipio> findAll(Long idDepartamento);

    public List<Municipio> findAllByDepto(Long idDepto);
    
     public List<Municipio> findAllByProvincia(Long idProvincia);

    public List<Municipio> findAll(Long idProvincia, Long idDepartamento, String nombre, String codigoPostal);

    public Municipio find(String nombre, String nombreProvincia);

    public List<Municipio> findAll(Long idProvincia, Long idDepartamento,String nombre, String codigoPostal, int first, int pageSize);

    public Long countAll(Long idProvincia, Long idDepartamento,String nombre, String codigoPostal);
    
}
