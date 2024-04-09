package com.example.turismotfg.Entity;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

public class CurrentUser implements Serializable {
    private static final String SHARE_PREFS = "SHARE_PREFS";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROL = "rol";

    private SharedPreferences sharedPreferences;

    public CurrentUser(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
    }

    // Constructor sin argumentos necesario para la deserialización
    public CurrentUser() {
    }

    public void saveUserDetails(String name, String surname, String email, String rol) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SURNAME, surname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROL, rol);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public String getSurname() {
        return sharedPreferences.getString(KEY_SURNAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getRol() {
        return sharedPreferences.getString(KEY_ROL, "");
    }
}
