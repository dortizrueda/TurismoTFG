package com.example.turismotfg.DAO;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class guideDAO {
    private FirebaseFirestore firestore;
    private Context context;

    public void getGuideId(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore.getInstance().collection("guides")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
