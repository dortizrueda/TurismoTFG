package com.example.turismotfg.DAO;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.turismotfg.Entity.Valoration;
import com.example.turismotfg.GuideProfile;
import com.example.turismotfg.interfaces.ValorationList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class valoracionesDAO {
    private static final String TAG = "ValoracionesDAO";
    private FirebaseFirestore firestore;
    private CollectionReference valoracionesRef;
    private Context context;


    public valoracionesDAO(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        valoracionesRef = firestore.collection("valoraciones");
    }


    public static void getGuideId(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore.getInstance().collection("guides")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public static void getAllValByGuide(String guide_name, ValorationList callbackList) {
        List<Valoration> valoraciones = new ArrayList<>();
        getGuideId(guide_name, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String guideId = document.getId();
                    FirebaseFirestore.getInstance().collection("valoraciones")
                            .whereEqualTo("guideId", guideId)
                            .get()
                            .addOnSuccessListener(query -> {
                                for (DocumentSnapshot document1 : query.getDocuments()) {
                                    Valoration valoration = document1.toObject(Valoration.class);
                                    if (valoration != null) {
                                        valoraciones.add(valoration);
                                    }
                                }
                                //Llamada a callback
                                callbackList.onListValoration(valoraciones);
                            })
                            .addOnFailureListener(exception -> {
                                Log.e("TAG", "Error al obtener valoraciones", exception);
                                callbackList.onError(task.getException());
                            });
                }
            } else {
                Log.e("TAG", "Error al obtener ID de guía", task.getException());
                callbackList.onError(task.getException());
            }
        });
    }


    public void getValoracionByGuideandUser(String user_id, String name, RatingBar ratingBar) {
        getGuideId(name, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String guideId = document.getId();
                    FirebaseFirestore.getInstance().collection("valoraciones")
                            .whereEqualTo("guideId", guideId)
                            .whereEqualTo("userId", user_id)
                            .get()
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    for (DocumentSnapshot document2 : task2.getResult()) {
                                        Valoration valoration = document2.toObject(Valoration.class);
                                        if (valoration != null) {
                                            float userRating = valoration.getRating();
                                            ratingBar.setRating(userRating);
                                        }
                                    }
                                } else {
                                    Log.e("GuideProfile", "Error al obtener la valoración previa:", task2.getException());
                                }
                            });
                }
            } else {
                Log.e("GuideProfile", "Error al obtener la guía:", task.getException());
                Toast.makeText(context, "Error al obtener la guía", Toast.LENGTH_SHORT).show();
            }
        });
    }
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
    public void addValoracion(String user_uid, String name, float value) {
        getGuideId(name, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String guideId = document.getId();
                    FirebaseFirestore.getInstance().collection("valoraciones")
                            .whereEqualTo("guideId", guideId)
                            .whereEqualTo("userId", user_uid)
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    boolean valorado = !task1.getResult().isEmpty();
                                    if (valorado) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Ya has valorado esta guía. ¿Deseas modificar tu valoración?")
                                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Valoration v = new Valoration(guideId,user_uid,value);
                                                        updateValoracion(v);
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    } else {
                                        Valoration v = new Valoration(guideId,user_uid,value);
                                        adValoration(v);
                                    }
                                } else {
                                    Log.e("GuideProfile", "Error al verificar la valoración:", task1.getException());
                                    Toast.makeText(context, "Error al verificar la valoración", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Log.e("GuideProfile", "Error al obtener la guía:", task.getException());
                Toast.makeText(context, "Error al obtener la guía", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateValoracion(Valoration v) {
        getValoracionId(v.getUserId(),v.getGuideId(),id_valoration ->{
            FirebaseFirestore.getInstance().collection("valoraciones").document(id_valoration)
                    .set(v)
                    .addOnSuccessListener(document -> {
                        Toast.makeText(context, "Valoración actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(context, "Error al actualizar la valoración", Toast.LENGTH_SHORT).show();
                        Log.e("GuideProfile", "Error al actualizar la valoración:", exception);
                    });
        },e -> {
            Toast.makeText(context, "Error al obtener el ID de la valoración: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("GuideProfile", "Error al obtener el ID de la valoración:", e);
        });

    }

    private void adValoration(Valoration v){
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


