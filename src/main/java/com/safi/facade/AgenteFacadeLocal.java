/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Agente;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface AgenteFacadeLocal {

    void create(Agente agente);

    void edit(Agente agente);

    void remove(Agente agente);

    Agente find(Object id);

    List<Agente> findAll();

    List<Agente> findRange(int[] range);

    int count();

    public List<Agente> findAll(boolean estado);

    public List<Agente> findAll(String appelidoNombre);

    public void create(Long dni, Integer legajo, String apelidoNombre) throws Exception;

    public void edit(Long idAgente, Long dni, Integer legajo, String apelidoNombre) throws Exception;

    public void remove(Long idAgente) throws Exception;

    public List<Agente> findAll(Long dni, Integer legajo, String apellidoNombre);

    public List<Agente> findAllNotCB(boolean estado);

    public List<Agente> findAll(Long dni, Integer legajo, String apellidoNombre,int first, int pageSize);

    public Long countAll(Long dni, Integer legajo, String apellidoNombre);
    
}
