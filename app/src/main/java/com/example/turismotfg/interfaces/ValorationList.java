package com.example.turismotfg.interfaces;

import com.example.turismotfg.Entity.Valoration;

import java.util.List;

public interface ValorationList {
    void onListValoration(List<Valoration>valoraciones);
    void onError(Exception exception);
}
