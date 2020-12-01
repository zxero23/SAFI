package com.safi.enums;

/**
 * @author Doroñuk Gustavo
 */
public enum GrupoUsuarioEnum {
    
    SUPER_ADMIN(1L, "SUPER ADMIN"),
    ADMIN(2L, "SUPER ADMIN"),
    MIGRACION(3L, "MIGRACIÓN"),
    DIRECCION_ANALISIS_INFORMATICA(4L, "DIRECCIÓN DE ANÁLISIS E INFORMÁTICA"),
    SERVICIO_ADMINISTRATIVO(5L, "SERVICIO"),
    MINISTERIO_HACIENDA(9L, "MINISTERIO DE HACIENDA"),
    DIRECTOR_REGISTRO_PROVEEDORES(10L, "DIRECCIÓN DEL REGISTRO DE PROVEEDORES"),
    PROVEEDORES(11L, "PROVEEDORES"),
    SERVICIOADMINISTRATIVOREPORTES(12L, "SERVICIO - REPORTES"),
    DIRECCION_GENERAL_DE_PRESUPUESTO(13L, "DIRECCIÓN GENERAL DE PRESUPUESTO"),
    REPORTES(14L, "REPORTES"),
    HTC(8L, "HONORABLE TRIBUNAL DE CUENTAS");
    
    private Long id;
    private String nombre;
    
    GrupoUsuarioEnum(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }    
    
    
    
}
