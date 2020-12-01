/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safi.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 *
 * @author Angel Flores
 */
@Entity
@Table(name = "notificaciones_servicios")
public class NotificacionServicio implements Serializable{
    @Id
    private Long notificacion_id;
    private Long servicio_id;

    public Long getNotificacion_id() {
        return notificacion_id;
    }

    public void setNotificacion_id(Long notificacion_id) {
        this.notificacion_id = notificacion_id;
    }

    public Long getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(Long servicio_id) {
        this.servicio_id = servicio_id;
    }
    
    
}