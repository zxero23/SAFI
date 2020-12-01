/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.utilidad;

/**
 *
 * @author ccpm
 */
public class Recurso {
    
    // nomenclador de recurso propio
    private String servicio;
    private String cla_ins;
    private String cod_org;
    private String caracter;
    private String genero;
    private String jurisdiccion;
    private String tipo_rec;
    private String cla_rec;
    private String nroConcepto;
    private String detalleConcepto;
    private String ejercicio;
    /*El siguiente atributo se declara para concatenar el código completo más el nombre del cla. rec.*/
    private String codigoCompleto;

    public Recurso(String servicio, String cla_ins, String cod_org, String caracter, String genero, String jurisdiccion, String tipo_rec, String cla_rec, String nroConcepto, String detalleConcepto,String ejercicio, String codigoCompleto) {
        this.servicio = servicio;
        this.cla_ins = cla_ins;
        this.cod_org = cod_org;
        this.caracter = caracter;
        this.genero = genero;
        this.jurisdiccion = jurisdiccion;
        this.tipo_rec = tipo_rec;
        this.cla_rec = cla_rec;
        this.nroConcepto = nroConcepto;
        this.detalleConcepto = detalleConcepto;
        this.ejercicio=ejercicio;
        this.codigoCompleto = codigoCompleto;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCla_ins() {
        return cla_ins;
    }

    public void setCla_ins(String cla_ins) {
        this.cla_ins = cla_ins;
    }

    public String getCod_org() {
        return cod_org;
    }

    public void setCod_org(String cod_org) {
        this.cod_org = cod_org;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getJurisdiccion() {
        return jurisdiccion;
    }

    public void setJurisdiccion(String jurisdiccion) {
        this.jurisdiccion = jurisdiccion;
    }

    public String getTipo_rec() {
        return tipo_rec;
    }

    public void setTipo_rec(String tipo_rec) {
        this.tipo_rec = tipo_rec;
    }

    public String getCla_rec() {
        return cla_rec;
    }

    public void setCla_rec(String cla_rec) {
        this.cla_rec = cla_rec;
    }

    public String getNroConcepto() {
        return nroConcepto;
    }

    public void setNroConcepto(String nroConcepto) {
        this.nroConcepto = nroConcepto;
    }

    public String getDetalleConcepto() {
        return detalleConcepto;
    }

    public void setDetalleConcepto(String detalleConcepto) {
        this.detalleConcepto = detalleConcepto;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getCodigoCompleto() {
        return codigoCompleto;
    }

    public void setCodigoCompleto(String codigoCompleto) {
        this.codigoCompleto = codigoCompleto;
    }
    
}