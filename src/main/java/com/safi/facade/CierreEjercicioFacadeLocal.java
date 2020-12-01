/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.CierreEjercicio;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface CierreEjercicioFacadeLocal {

    void create(CierreEjercicio cierreEjercicio);

    void edit(CierreEjercicio cierreEjercicio);

    void remove(CierreEjercicio cierreEjercicio);

    CierreEjercicio find(Object id);

    List<CierreEjercicio> findAll();

    List<CierreEjercicio> findRange(int[] range);

    int count();

    public void create(Long idEjercicio, Long idServicio, Long idUsuario) throws Exception;

    public void edit(Long idCierreEjercicio, Long idEjercicio, Long idServicio, Long idUsuario) throws Exception;

    public void remove(Long idCierreEjercicio) throws Exception;

    public List<CierreEjercicio> findAll(boolean estado);

    public List<CierreEjercicio> findAll(Long idEjercicio, Long idServicio);

    
}
