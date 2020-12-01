/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Feriado;

/**
 *
 * @author Doroñuk Gustavo
 */
@Local
public interface FeriadoFacadeLocal {

    void create(Feriado feriado);

    void edit(Feriado feriado);

    void remove(Feriado feriado);

    Feriado find(Object id);

    List<Feriado> findAll();

    List<Feriado> findRange(int[] range);

    int count();

    public void create(String nombre, Date fecha) throws Exception;

    public List<Feriado> findAll(boolean estado);

    public List<Feriado> findFeriados(List<Long> idFeriados);

    public void remove(Long idFeriado) throws Exception;

    public void edit(Long idFeriado, String nombre, Date fecha) throws Exception;

    public boolean isFeriado(Date fecha);

    public List<Feriado> findAllFeriadoLogin(boolean estado);

    public List<Feriado> findAll(String nombreBuscar, Date fechaDesdeBuscar, Date fechaHastaBuscar);

    public List<Feriado> findAll(String nombreBuscar, Date fechaDesdeBuscar, Date fechaHastaBuscar, int first, int pageSize);

    public Long countAll(String nombreBuscar, Date fechaDesdeBuscar, Date fechaHastaBuscar);

}
