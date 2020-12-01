package com.safi.enums;

/**
 * Definición de los Tipos de Servicios
 * @author Diana Olivera
 */
public enum TipoServicioEnum {
    
    SERVICIO(1L, "Servicio Administrativo"),
    DIR_ADM(2L, "Dirección de Administración");
    
    private Long id;
    private String name;
    
    TipoServicioEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }    
}