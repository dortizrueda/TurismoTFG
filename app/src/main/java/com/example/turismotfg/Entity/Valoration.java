package com.example.turismotfg.Entity;

public class Valoration {
    private String guideId;
    private String userId;
    private float rating;

    public Valoration() {
        // Constructor vacío requerido por Firebase
    }

    public Valoration(String guideId, String userId, float rating) {
        this.guideId = guideId;
        this.userId = userId;
        this.rating = rating;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
