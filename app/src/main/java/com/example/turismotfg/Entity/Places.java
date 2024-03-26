package com.example.turismotfg.Entity;

import java.io.Serializable;
import java.util.List;

public class Places implements Serializable {
    private String nombre;
    private String descripcion;
    private List<String> imagenes;
    private double latitud;
    private double longitud;

    public Places() {
        // Constructor vacío requerido por Firestore
    }

    public Places(String nombre, String descripcion, List<String> imagenes, double latitud, double longitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
