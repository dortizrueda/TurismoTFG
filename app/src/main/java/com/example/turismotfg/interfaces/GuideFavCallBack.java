package com.example.turismotfg.interfaces;
/**
 * Interfaz que define callbacks para comprobar si una
 * guía está marcada como favorita.
 */
public interface GuideFavCallBack {
    /**
     * Método para comprobar si una guía es fav o no.
     * @param fav Booleano ; true --> favorito; false --> no favorito.
     */
    void isFav(boolean fav);
    /**
     * Método para manejar errores.
     * @param exception Excepción que describe el error.
     */
    void onError(Exception exception);
}
