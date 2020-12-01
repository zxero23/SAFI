/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.enums;

/**
 * Definicion de los tipos de estado.
 * @author Angel Flores
 */
public enum TipoEstadoEnum {
    
    ACTIVO(1L, "ACTIVO"),
    CERRADO(2L, "CERRADO"),
    FORMULACION(3L, "FORMULACIÓN");
    
    private Long id;
    private String estado;
    
    TipoEstadoEnum(Long id, String estado) {
        this.id = id;
        this.estado = estado;
    }
    
    public Long getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    } 
}
