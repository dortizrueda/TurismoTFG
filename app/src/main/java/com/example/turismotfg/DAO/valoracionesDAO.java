package com.example.turismotfg.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.turismotfg.Entity.Valoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class valoracionesDAO {
    private FirebaseFirestore firestore;
    private Context context;
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
