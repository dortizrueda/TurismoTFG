package com.example.turismotfg.Managers;

import android.content.Context;
import android.util.Log;

import com.example.turismotfg.DAO.guideDAO;
import com.example.turismotfg.interfaces.GuideFavCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.ArrayList;
/**
 * Clase utilizada para manejar las
 * guías del sistema.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class guideManager {
    private FirebaseFirestore firestore;
    private Context context;
    guideDAO guideDao=new guideDAO();
    /**
     * Constructor de la clase guideManager.
     * @param context Contexto de la aplicación.
     */
    public guideManager(Context context) {
        this.context=context;
        firestore = FirebaseFirestore.getInstance();
    }


    /**
     * Método que controla si una guía pertenece a las favoritas de
     * x-usuario.
     * @param name nombre de la guía.
     * @param uid Listener que maneja que se completa la tarea.
     * @param favCallBack Callback que maneja si una guía es favorita.
     */
    public void checkGuide(String uid, String name, GuideFavCallBack favCallBack) {
        guideDao.getGuideId(name, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String guideId = document.getId();
                    FirebaseFirestore.getInstance().collection("users")
                            .document(uid)
                            .get()
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    DocumentSnapshot userSnapshot = task2.getResult();
                                    if (userSnapshot.exists()) {
                                        List<DocumentReference> favoriteGuideRefs = (List<DocumentReference>) userSnapshot.get("favorites");
                                        if (favoriteGuideRefs != null) {
                                            List<String> favorites_guides = new ArrayList<>();
                                            for (DocumentReference ref : favoriteGuideRefs) {
                                                favorites_guides.add(ref.getId());
                                            }
                                            Log.d("Favorites", "Lista de favoritos: " + favorites_guides.toString());
                                            if (favorites_guides.contains(guideId)) {
                                                // La guía es favorita para el usuario
                                                favCallBack.isFav(true);
                                            } else {
                                                // La guía no es favorita para el usuario
                                                favCallBack.isFav(false);
                                            }
                                        } else {
                                            favCallBack.isFav(false);
                                        }
                                    } else {
                                        favCallBack.isFav(false);
                                    }
                                } else {
                                    favCallBack.onError(task2.getException());
                                }
                            });
                }
            } else {
                favCallBack.onError(task.getException());
            }
        });
    }



}
