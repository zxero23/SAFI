/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.enums;

/**
 *
 * @author ccpm
 */
public enum TipoRecursoModificadoEnum {
    DECRETO(1L, "DECRETO"),    
    DISPOSICION(2L,"DISPOSICIÓN"),
    RESOLUCION(3L, "RESOLUCIÓN");
    private static TipoRecursoModificadoEnum UNKNOWN;
    
    private Long id;
    private String name;

    private TipoRecursoModificadoEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static TipoRecursoModificadoEnum getById(Long id) {
    for(TipoRecursoModificadoEnum e : values()) {
        if(e.id.equals(id)) return e;
    }
    return UNKNOWN;
    }
   
    
    
}
