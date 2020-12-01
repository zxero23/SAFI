package com.safi.enums;

/**
 * Definición de todos los manuales del sistema.
 * @author Doroñuk Gustavo
 */
public enum ManualEnum {
    
    MANUAL_FONDOS_VALORES_ADMINISTRADOR(1, "Fondos y Valores Administrador", "ManualFondosValores-Administrador.pdf", "ManualFondosValores-Administrador"),
    MANUAL_FONDOS_VALORES_OPERADOR(2, "Fondos y Valores Operador", "ManualFondosValores-Operador.pdf", "ManualFondosValores-Operador"),
    MANUAL_PROVEEDORES(3, "Proveedores", "ManualProveedores.pdf", "ManualProveedores"),
    MANUAL_FONDOS_VALORES_REPORTES(4, "Fondos y Valores Reportes", "ManualFondosValores-Reportes.pdf", "ManualFondosValores-Reportes"),
    MANUAL_FONDOS_VALORES_PRESUPUESTO(5, "Fondos y Valores Presupuesto", "ManualFondosValores-Presupuesto.pdf", "ManualFondosValores-Presupuesto");
    
    private int id;
    private String name;
    private String path;
    private String fileName;
    
    ManualEnum(int id, String name, String path, String fileName) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.fileName = fileName;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getFileName() {
        return fileName;
    }
    
}