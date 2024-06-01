package com.example.turismotfg.Entity;
/**
 * Clase que representa al objeto valoración.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class Valoration {
    private String guideId;
    private String userId;
    private float rating;
    /**
     * Constructor sin argumentos de
     * la clase Valoration.
     */
    public Valoration() {
    }
    /**
     * Constructor de la clase Valoration.
     *
     * @param guideId id de la guía.
     * @param userId id del usuario.
     * @param rating calificación.
     */
    public Valoration(String guideId, String userId, float rating) {
        this.guideId = guideId;
        this.userId = userId;
        this.rating = rating;
    }
    /**
     * Método que devuelve la id de la guía.
     *
     * @return string con el id de la guía.
     */
    public String getGuideId() {
        return guideId;
    }
    /**
     * Método que almacena la id de la guía.
     *
     * @param guideId  string con el id de la guía.
     */
    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }
    /**
     * Método que devuelve el id del usuario.
     *
     * @return string con el id del usuario.
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Método que almacena el id del usuario.
     *
     * @param userId string con el id del usuario.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Método que devuelve la valoración.
     *
     * @return float con la valoración.
     */
    public float getRating() {
        return rating;
    }
    /**
     * Método que almacena la valoración.
     *
     * @param rating  float con la valoración.
     */
    public void setRating(float rating) {
        this.rating = rating;
    }
}
