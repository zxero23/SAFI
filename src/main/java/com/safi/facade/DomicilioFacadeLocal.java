/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Domicilio;

/**
 *
 * @author ASC Doroñuk Gustavo
 */
@Local
public interface DomicilioFacadeLocal {

    void create(Domicilio domicilio);

    void edit(Domicilio domicilio);

    void remove(Domicilio domicilio);

    Domicilio find(Object id);

    List<Domicilio> findAll();

    List<Domicilio> findRange(int[] range);

    int count();
    
    public List<Domicilio> findAll(boolean estado);

    public void create(String domicilio, Long idMunicipio, Long idDepartamento, Long idProvincia) throws Exception;

    public void edit(Long id, String domicilio, Long idMunicipio, Long idDepartamento, Long idProvincia) throws Exception;

    public void remove(Long id) throws Exception;
    
}
