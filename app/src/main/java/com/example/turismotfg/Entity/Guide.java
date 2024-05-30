package com.example.turismotfg.Entity;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Guide {
    private String name;
    private String description;
    private String audioURL; // URL del archivo de audio
    private String city;
    private int numPlaces; // Número de lugares asociados a la guía
    private float media;
    private String creator; // UID del creador de la guía
    private List<DocumentReference> places;

    // Constructor por defecto
    public Guide() {
    }

    // Constructor con parámetros
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

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public float getMedia(){return media;}
    public void setMedia(float media){this.media=media;}


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioUrl() {
        return audioURL;
    }

    public void setAudioUrl(String audioFileUrl) {
        this.audioURL = audioFileUrl;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<DocumentReference> getPlaces() {
        return places;
    }

    public void setPlaces(List<DocumentReference> places) {
        this.places = places;
    }
}


