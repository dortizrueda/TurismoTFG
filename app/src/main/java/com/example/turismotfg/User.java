package com.example.turismotfg;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    String surname;
    String email;
    String password;
    String username;
    Rol rol;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public Rol getRol() { return rol; }
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public void setRol(Rol rol) { this.rol = rol; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String email, String name, String surname, String password, Rol rol) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.rol = rol;
    }

    public User() {
    }
}





