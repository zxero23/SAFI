/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.safi.entity.Mail;

/**
 *
 * @author Facundo González
 */
@Stateless
public class MailFacade extends AbstractFacade<Mail> implements MailFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    
    @EJB
    UsuarioFacadeLocal usuarioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MailFacade() {
        super(Mail.class);
    }
    
    
    @Override
    public void crear(String destinatario, String asunto, String textoMensaje, Date fecha, Long idUsuario, boolean adjuntos){        
        try{
            Mail mailAux = new Mail();
            mailAux.setDestinatario(destinatario);
            mailAux.setAsunto(asunto);
            mailAux.setTextoMensaje(textoMensaje);
            mailAux.setFecha(fecha);
            mailAux.setUsuario(this.usuarioFacade.find(idUsuario));
            mailAux.setAdjuntos(adjuntos);
            this.create(mailAux);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    
}
