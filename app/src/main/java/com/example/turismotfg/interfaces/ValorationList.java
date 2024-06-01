package com.example.turismotfg.interfaces;

import com.example.turismotfg.Entity.Valoration;

import java.util.List;
/**
 * Interfaz que define callbacks para
 * devolver una lista de valoraciones.
 */
public interface ValorationList {
    /**
     * Método para devolver la lista de valoraciones.
     * @param valoraciones Lista de valoraciones.
     */
    void onListValoration(List<Valoration>valoraciones);
    /**
     * Método para manejar errores.
     * @param exception Excepción que describe el error.
     */
    void onError(Exception exception);
}
