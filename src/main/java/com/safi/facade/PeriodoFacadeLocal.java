/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Periodo;

/**
 *
 * @author eugenio
 */
@Local
public interface PeriodoFacadeLocal {

    void create(Periodo periodo);

    void edit(Periodo periodo);

    void remove(Periodo periodo);

    Periodo find(Object id);

    List<Periodo> findAll();

    List<Periodo> findRange(int[] range);

    int count();

    public List<Periodo> findAll(Date fechaDesde, Date fechaHasta);

    public void create(Date fechaDesde, Date fechaHasta) throws Exception;

    public void edit(Long idPeriodo, Date fechaDesde, Date fechaHasta) throws Exception;

    public void remove(Long idPeriodo) throws Exception;

    public List<Periodo> findAll(Date fechaDesde, Date fechaHasta, int first, int pageSize);

    public Long countAll(Date fechaDesde, Date fechaHasta);
    
}
