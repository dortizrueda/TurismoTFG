package com.example.turismotfg.Entity;

import java.io.Serializable;
import java.util.List;
/**
 * Clase que representa al objeto de lugares.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class Places implements Serializable {
    private String nombre;
    private String descripcion;
    private List<String> imagenes;
    private double latitud;
    private double longitud;

    private String audioFile;
    /**
     * Constructor de la clase Guide sin parámetros.
     *
     */
    public Places() {
        // Constructor vacío requerido por Firestore
    }
    /**
     * Constructor de la clase Guide.
     *
     * @param nombre nombre del lugar.
     * @param audioFile audio explicativo del lugar.
     * @param longitud longitud del lugar.
     * @param latitud latitud del lugar.
     * @param imagenes imágenes asociadas del lugar.
     * @param descripcion descripción del lugar.
     */
    public Places(String nombre, String descripcion, List<String> imagenes, double latitud, double longitud,String audioFile) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.latitud = latitud;
        this.longitud = longitud;
        this.audioFile=audioFile;
    }
    /**
     * Método que devuelve el nombre del lugar.
     *
     * @return string con el nombre del lugar.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Método que almacena el nombre del lugar.
     *
     * @param nombre  string con el nombre del lugar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Método que devuelve la descripción del lugar.
     *
     * @return string con la descripción del lugar.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * Método que almacena la descripción del lugar.
     *
     * @param descripcion  string con la descripción del lugar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * Método que devuelve las imágenes del lugar.
     *
     * @return List<String> que almacena las rutas de las imágenes.
     */
    public List<String> getImagenes() {
        return imagenes;
    }
    /**
     * Método que almacena las imágenes del lugar.
     *
     * @param imagenes  List<String> que almacena las rutas de las imágenes.
     */
    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }
    /**
     * Método que devuelve la latitud del lugar.
     *
     * @return double con la latitud del lugar.
     */
    public double getLatitud() {
        return latitud;
    }
    /**
     * Método que almacena la latitud del lugar.
     *
     * @param latitud  double con la latitud del lugar.
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    /**
     * Método que devuelve la longitud del lugar.
     *
     * @return double con la longitud del lugar.
     */
    public double getLongitud() {
        return longitud;
    }
    /**
     * Método que almacena la longitud del lugar.
     *
     * @param longitud  double con la longitud del lugar.
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    /**
     * Método que devuelve el archivo de audio del lugar.
     *
     * @return string con la ruta al archivo de audio del lugar.
     */
    public String getAudioFile(){return audioFile;}
    /**
     * Método que almacena el archivo de audio del lugar.
     *
     * @param audioFile string con la ruta al archivo de audio del lugar.
     */
    public void setAudioFile(String audioFile){this.audioFile=audioFile;}
}
