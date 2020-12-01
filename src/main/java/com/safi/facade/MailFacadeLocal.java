/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Usuario;
import com.safi.entity.Mail;

/**
 *
 * @author Facundo González
 */
@Local
public interface MailFacadeLocal {

    void create(Mail mail);

    void edit(Mail mail);

    void remove(Mail mail);

    Mail find(Object id);

    List<Mail> findAll();

    List<Mail> findRange(int[] range);

    int count();
    
    public void crear(String destinatario, String asunto, String textoMensaje, Date fecha, Long idUsuario, boolean adjuntos);
    
}
