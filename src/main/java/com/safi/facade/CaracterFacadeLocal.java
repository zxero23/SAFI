/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Caracter;

/**
 *
 * @author ccpm
 */
@Local
public interface CaracterFacadeLocal {

    void create(Caracter caracter);

    void edit(Caracter caracter);

    void remove(Caracter caracter);

    Caracter find(Object id);

    List<Caracter> findAll();

    List<Caracter> findRange(int[] range);

    int count();
    
}
