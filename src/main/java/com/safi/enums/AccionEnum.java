package com.safi.enums;

/**
 * Definición de las acciones de auditoría.
 * @author Diana Olivera
 */
public enum AccionEnum {
    
    ALTA_MOVIMIENTO(1, "Alta movimiento"),
    REVERSION_MOVIMIENTO(2, "Reversión movimiento"),
    ALTA_RECURSO_PROPIO(3, "Alta recurso propio"),
    EDICION_RECURSO_PROPIO(4, "Edición recurso propio"),
    ELIMINACION_RECURSO_PROPIO(5, "Eliminación recurso propio"),
    ALTA_MODIFICACION_RECURSO_PROPIO(6, "Alta modificación recurso propio"),
    GENERACION_REPORTE(7, "Generación reporte"),
    CIERRE_EJERCICIO(8,"Cierre ejercicio"),
    APERTURA_EJERCICIO(9,"Apertura ejercicio"),
    EDICION_EJERCICIO(10,"Editar ejercicio"),
    LOGIN(11,"Login"),
    ALTA_USUARIO(12,"ALTA DE USUARIO"),
    EDICION_USUARIO(13,"EDICIÓN DE USUARIO"),
    CAMBIO_CONTRASENIA(14,"CAMBIO DE CONTRASEÑA"),
    BUSQUEDA_EXPEDIENTE(15,"BÚSQUEDA DE EXPEDIENTE");
    
    private int id;
    private String name;
    
    AccionEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }    
}