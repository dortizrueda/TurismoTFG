package com.example.turismotfg.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.turismotfg.Entity.Valoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * Clase utilizada para el acceso a los datos
 * del objeto Valoraciones.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class valoracionesDAO {
    private FirebaseFirestore firestore;
    private Context context;

    /**
     * Método que obtiene el id de una valoración.
     * @param userId ID del usuario.
     * @param guideId ID de la guía.
     * @param successListener Listener que maneja que la tarea sea completada con éxito.
     * @param failureListener Listener que maneja la tarea en el caso que sea fallida.
     */
    public void getValoracionId(String userId, String guideId, OnSuccessListener<String> successListener, OnFailureListener failureListener) {
        FirebaseFirestore.getInstance().collection("valoraciones")
                .whereEqualTo("userId", userId)
                .whereEqualTo("guideId", guideId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String valoracionId = documentSnapshot.getId();
                        successListener.onSuccess(valoracionId);
                    } else {
                        failureListener.onFailure(new Exception("No se encontró ninguna valoración para el usuario y la guía proporcionados"));
                    }
                })
                .addOnFailureListener(failureListener);
    }
    /**
     * Método que actualiza una valoración existente.
     * @param v Objeto Valoración.
     * @param context Contexto de la aplicación.
     */
    public void updateValoracion(Valoration v,Context context) {
        getValoracionId(v.getUserId(), v.getGuideId(), id_valoration -> {
            FirebaseFirestore.getInstance().collection("valoraciones").document(id_valoration)
                    .set(v)
                    .addOnSuccessListener(document -> {
                        Toast.makeText(context, "Valoración actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(context, "Error al actualizar la valoración", Toast.LENGTH_SHORT).show();
                        Log.e("GuideProfile", "Error al actualizar la valoración:", exception);
                    });
        }, e -> {
            Toast.makeText(context, "Error al obtener el ID de la valoración: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("GuideProfile", "Error al obtener el ID de la valoración:", e);
        });
    }
    /**
     * Método que agrega una nueva valoración.
     * @param v Objeto Valoración.
     * @param context Contexto de la aplicación.
     */
    public void adValoration(Valoration v,Context context){
        FirebaseFirestore.getInstance().collection("valoraciones")
                .add(v)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Valoración exitosa", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al valorar la guía", Toast.LENGTH_SHORT).show();
                    Log.e("GuideProfile", "Error al valorar la guía:", e);
                });
    }
}
