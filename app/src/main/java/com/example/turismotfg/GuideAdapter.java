package com.example.turismotfg;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Entity.Guide;
import com.example.turismotfg.Entity.Places;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private Context context;
    private List<Guide> guideList;
    private List<Places> placeList;

    private FirebaseFirestore firestore;

    public GuideAdapter(Context context, List<Guide> guideList, ArrayList<Places> placeList) {
        this.context = context;
        this.guideList = guideList;
        this.placeList = placeList;
        firestore=FirebaseFirestore.getInstance();
    }

    //Asignamos la vista para este item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false);
        return new ViewHolder(view, context);  // Pasa el contexto al ViewHolder
    }

    //Asocio un conjunto a una vista dentro de un viewHolder;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewName.setText(guideList.get(position).getName());
        holder.textViewDescription.setText(guideList.get(position).getDescription());
        Guide guide=guideList.get(position);

        List<DocumentReference>places=guide.getPlaces();
        if (!places.isEmpty()){
            StringBuilder StringInfo=new StringBuilder();
            for (DocumentReference ref : places) {
                firestore.collection("places").document(ref.getId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // Obtener StringInformación del lugar
                                        String name = document.getString("name");
                                        String description = document.getString("description");
                                        List<String> images = (List<String>) document.get("images");
                                        Double latitude = document.getDouble("latitude");
                                        Double longitude = document.getDouble("longitude");

                                        if (name != null && description != null && images != null &&
                                                latitude != null && longitude != null) {
                                            Places p = new Places(name, description, images, latitude, longitude);
                                            placeList.add(p);
                                        }

                                    } else {
                                        Toast.makeText(context, "Error al obtener información del lugar", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Error al obtener el documento
                                    Toast.makeText(context, "Error al obtener información del lugar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }else{
            Toast.makeText(context, "No existen lugares en esta guía...", Toast.LENGTH_SHORT).show();
        }
        holder.guideCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, GuideProfile.class);
                intent.putExtra("name", guideList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("description", guideList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("placeList",(Serializable) placeList );
                // Pasa otras propiedades de Guide según sea necesario
                holder.context.startActivity(intent);
            }
        });
    }

    //Numero de guias que hay en la BD
    @Override
    public int getItemCount() {
        return guideList.size();
    }

    //Clase estatica...
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName,textViewDescription,textViewPlaces;

        CardView guideCard;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            guideCard = itemView.findViewById(R.id.recyclerCardGuide);
        }
    }
}
