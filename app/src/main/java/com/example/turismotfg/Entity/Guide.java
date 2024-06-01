package com.example.turismotfg.Entity;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;
/**
 * Clase que representa al objeto guía.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class Guide {
    private String name;
    private String description;
    private String audioURL; // URL del archivo de audio
    private String city;
    private int numPlaces; // Número de lugares asociados a la guía
    private float media;
    private String creator; // UID del creador de la guía
    private List<DocumentReference> places;

    /**
     * Constructor sin argumentos de
     * la clase Guide.
     */
    public Guide() {
    }

    /**
     * Constructor de la clase Guide.
     *
     * @param name nombre de la guía.
     * @param city ciudad de la guía.
     * @param audioURL audio explicativo de la guía.
     * @param media valoración media de la guía.
     * @param numPlaces numero de lugares asociados a la guía.
     * @param creator id del creador de la guía.
     * @param description descripción de la guía.
     * @param places lugares asociados a la guia.
     */
    public Guide(String name, String description, String audioURL,String city, int numPlaces, String creator, List<DocumentReference> places,float media) {
        this.name = name;
        this.description = description;
        this.audioURL = audioURL;
        this.city=city;
        this.numPlaces = numPlaces;
        this.creator = creator;
        this.places = places;
        this.media=media;
    }

    /**
     * Método que devuelve el nombre de la guía.
     *
     * @return string con el nombre de la guía.
     */
    public String getName() {
        return name;
    }
    /**
     * Método que almacena el nombre de la guía.
     *
     * @param name  string con el nombre de la guía.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Método que devuelve la media de la guía.
     *
     * @return float con la media de la guía.
     */
    public float getMedia(){return media;}
    /**
     * Método que almacena la media de la guía.
     *
     * @param media  float con la media de la guía.
     */
    public void setMedia(float media){this.media=media;}

    /**
     * Método que devuelve la descripción de la guía.
     *
     * @return string con la descripción de la guía.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Método que almacena la descripción de la guía.
     *
     * @param description string con la descripción de la guía.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Método que devuelve el audio de la guía.
     *
     * @return string con el audio de la guía.
     */
    public String getAudioUrl() {
        return audioURL;
    }
    /**
     * Método que almacena el audio de la guía.
     *
     * @param audioFileUrl  string con el audio de la guía.
     */
    public void setAudioUrl(String audioFileUrl) {
        this.audioURL = audioFileUrl;
    }
    /**
     * Método que devuelve la ciudad de la guía.
     *
     * @return string con la ciudad de la guía.
     */
    public String getCity() {
        return city;
    }
    /**
     * Método que almacena la ciudad de la guía.
     *
     * @param city string con la ciudad de la guía.
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * Método que devuelve el numero de lugares asociados de la guía.
     *
     * @return string con el numero de lugares asociados de la guía.
     */
    public int getNumPlaces() {
        return numPlaces;
    }
    /**
     * Método que almacena el numero de lugares asociados de la guía.
     *
     * @param numPlaces entero con el numero de lugares asociados de la guía.
     */
    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }
    /**
     * Método que devuelve el creador de la guía.
     *
     * @return int con el creador de la guía.
     */
    public String getCreator() {
        return creator;
    }
    /**
     * Método que almacena el creador de la guía.
     *
     * @param creator string con el creador de la guía.
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     * Método que devuelve la lista de los lugares asociados
     * de la guía.
     *
     * @return list lugares asociados a la guía.
     */
    public List<DocumentReference> getPlaces() {
        return places;
    }
    /**
     * Método que almacena la lista de los lugares asociados
     * de la guía.
     *
     * @param places lista de lugares asociados a la guía.
     */
    public void setPlaces(List<DocumentReference> places) {
        this.places = places;
    }
}


