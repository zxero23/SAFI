/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Angel Flores
 */
@Entity
@Table(name = "organismos_servicios")
public class OrganismoServicio implements Serializable {

    @Id
    private Long organismo_id;
    private Long servicios_id;

    public Long getOrganismos_id() {
        return organismo_id;
    }

    public void setOrganismos_id(Long organismo_id) {
        this.organismo_id = organismo_id;
    }

    public Long getServicios_id() {
        return servicios_id;
    }

    public void setServicios_id(Long servicios_id) {
        this.servicios_id = servicios_id;
    }

    
}
