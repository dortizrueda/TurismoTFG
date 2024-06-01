package com.example.turismotfg.DAO;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
/**
 * Clase utilizada para el acceso a los datos
 * del objeto Guías.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class guideDAO {
    private FirebaseFirestore firestore;
    private Context context;
    /**
     * Método que obtiene el id de una guía.
     * @param name nombre de la guía.
     * @param onCompleteListener Listener que maneja que se completa la tarea.
     */
    public void getGuideId(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore.getInstance().collection("guides")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
