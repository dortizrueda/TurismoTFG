package com.example.turismotfg.Entity;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
/**
 * Clase que representa al objeto user actual.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class CurrentUser implements Serializable {
    private static final String SHARE_PREFS = "SHARE_PREFS";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROL = "rol";

    private SharedPreferences sharedPreferences;
    /**
     * Constructor de la clase CurrentUser.
     * @param context Contexto de la aplicación.
     */
    public CurrentUser(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
    }
    /**
     * Constructor sin argumentos de
     * la clase CurrentUser.
     */
    public CurrentUser() {
    }
    /**
     * Método para guardar los detalles del usuario.
     *
     * @param name nombre del usuario.
     * @param surname apellidos del usuario.
     * @param rol rol del usuario.
     * @param email email del usuario.
     */
    public void saveUserDetails(String name, String surname, String email, String rol) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SURNAME, surname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROL, rol);
        editor.apply();
    }
    /**
     * Método que devuelve el nombre del usuario.
     *
     * @return string con el nombre del usuario.
     */
    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }
    /**
     * Método que devuelve los apellidos del usuario.
     *
     * @return string con los apellidos del usuario.
     */
    public String getSurname() {
        return sharedPreferences.getString(KEY_SURNAME, "");
    }
    /**
     * Método que devuelve el correo del usuario.
     *
     * @return string con el correo del usuario.
     */
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }
    /**
     * Método que devuelve el rol del usuario.
     *
     * @return  el rol del usuario.
     */
    public String getRol() {
        return sharedPreferences.getString(KEY_ROL, "");
    }
}
