package com.example.turismotfg.Managers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.turismotfg.DAO.guideDAO;
import com.example.turismotfg.DAO.valoracionesDAO;
import com.example.turismotfg.Entity.Valoration;
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

public class valoracionesManager {
    private static final String TAG = "ValoracionesDAO";
    private FirebaseFirestore firestore;
    private CollectionReference valoracionesRef;
    private Context context;
    static guideDAO guideDAO=new guideDAO();
    valoracionesDAO valoracionesDAO=new valoracionesDAO();


    public valoracionesManager(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        valoracionesRef = firestore.collection("valoraciones");
    }


    public static void getAllValByGuide(String guide_name, ValorationList callbackList) {
        List<Valoration> valoraciones = new ArrayList<>();
        guideDAO.getGuideId(guide_name, task -> {
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
        guideDAO.getGuideId(name, task -> {
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

    public void addValoracion(String user_uid, String name, float value) {
        guideDAO.getGuideId(name, task -> {
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
                                                        valoracionesDAO.updateValoracion(v,context);
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
                                        valoracionesDAO.adValoration(v,context);
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
}


