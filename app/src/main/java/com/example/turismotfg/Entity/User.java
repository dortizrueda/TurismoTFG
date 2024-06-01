package com.example.turismotfg.Entity;

import java.io.Serializable;
/**
 * Clase que representa al objeto User.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class User implements Serializable {
    String name;
    String surname;
    String email;
    String password;
    Rol rol;
    /**
     * Método que devuelve el nombre del usuario.
     *
     * @return string con el nombre del usuario.
     */
    public String getName() {
        return name;
    }
    /**
     * Método que devuelve los apellidos del usuario.
     *
     * @return string con los apellidos del usuario.
     */
    public String getSurname() {
        return surname;
    }
    /**
     * Método que devuelve el correo del usuario.
     *
     * @return string con el correo del usuario.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Método que devuelve la contraseña del usuario.
     *
     * @return string con la contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Método que devuelve el rol del usuario.
     *
     * @return  el rol del usuario.
     */
    public Rol getRol() { return rol; }
    /**
     * Método que almacena el rol del usuario.
     *
     * @param rol el rol del usuario.
     */
    public void setRol(Rol rol) { this.rol = rol; }
    /**
     * Método que almacena el nombre del usuario.
     *
     * @param name  string con el nombre del usuario.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Método que almacena los apellidos del usuario.
     *
     * @param surname  string con los apellidos del usuario.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /**
     * Método que almacena el correo del usuario.
     *
     * @param email  string con el correo del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Método que almacena la contraseña del usuario.
     *
     * @param password  string con la contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Constructor de la clase User.
     *
     * @param name nombre del usuario.
     * @param surname apellidos del usuario.
     * @param password contraseña del usuario.
     * @param rol rol del usuario.
     * @param email email del usuario.
     */
    public User(String email, String name, String surname, String password, Rol rol) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.rol = rol;
    }
    /**
     * Constructor de la clase User sin argumentos.
     */
    public User() {
    }
}





