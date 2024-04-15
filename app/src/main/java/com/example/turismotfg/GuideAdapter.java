package com.example.turismotfg;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private Context context;
    private List<Guide> guideList;
    private List<Places> placeList;

    private FirebaseFirestore firestore;

    public GuideAdapter(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
        firestore=FirebaseFirestore.getInstance();

    }

    //Asignamos la vista para este item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(guideList.get(position).getName());
        holder.description.setText(guideList.get(position).getDescription());

        Guide guide=guideList.get(position);


        List<DocumentReference>places=guide.getPlaces();
        Log.d("TAG", String.valueOf(places.size()));
        List<Places> placeList=new ArrayList<>();
        if (!places.isEmpty()){
            for (DocumentReference place_reference : places) {
                firestore.collection("places").document(place_reference.getId())
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
                                        String audioFile=document.getString("audio");

                                        if (name != null && description != null && images != null &&
                                                latitude != null && longitude != null) {
                                                Places place = new Places(name, description, images, latitude, longitude,audioFile);
                                                Log.d("TAG",place.getNombre());
                                                placeList.add(place);
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(context, "Error al obtener información del lugar", Toast.LENGTH_SHORT);
                                        toast.show();                                    }
                                } else {
                                    // Error al obtener el documento
                                    Toast toast = Toast.makeText(context, "Error al obtener los lugares", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });
            }
        }else{
            Toast toast = Toast.makeText(context, "No existen lugares en esta guia...", Toast.LENGTH_SHORT);
            toast.show();
        }
        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.context, GuideProfile.class);
                String creatorUid = guideList.get(holder.getAdapterPosition()).getCreator();
                if (creatorUid != null) {
                    Log.d("CORREO", creatorUid);
                } else {
                    Log.d("CORREO", "Creator UID is null");
                }
                    i.putExtra("name", guideList.get(holder.getAdapterPosition()).getName());
                    i.putExtra("description", guideList.get(holder.getAdapterPosition()).getDescription());
                    i.putExtra("creator",guideList.get(holder.getAdapterPosition()).getCreator());
                    i.putExtra("placeList",(Serializable) placeList );
                    holder.context.startActivity(i);
            }
        });
    }

    //Numero de guias que hay en la BD
    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        CardView recycler;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            recycler = itemView.findViewById(R.id.recyclerCardGuide);
            name = itemView.findViewById(R.id.textViewName);
            description = itemView.findViewById(R.id.textViewDescription);
        }

    }
    }

