package com.example.turismotfg;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Entity.Guide;
import com.example.turismotfg.Entity.Places;
import com.example.turismotfg.DAO.userDAO;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuideAdapterView extends RecyclerView.Adapter<GuideAdapterView.ViewHolder> {

    final userDAO userDAO;
    private Context context;
    private List<Guide> guideList;
    private List<Places> placeList;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public GuideAdapterView(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
        this.userDAO = new userDAO(context);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    @NonNull
    @Override
    public GuideAdapterView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide2, parent, false);
        return new GuideAdapterView.ViewHolder(view, context);  // Pasa el contexto al ViewHolder
    }



    @Override
    public void onBindViewHolder(@NonNull GuideAdapterView.ViewHolder holder, int position) {
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
                                            holder.bindImages(place.getImagenes());
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
                i.putExtra("name", guideList.get(holder.getAdapterPosition()).getName());
                i.putExtra("description", guideList.get(holder.getAdapterPosition()).getDescription());
                i.putExtra("placeList",(Serializable) placeList );
                holder.context.startActivity(i);
            }
        });

        holder.favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guide currentGuide = guideList.get(holder.getAdapterPosition());
                userDAO.addGuideToUserFavs(currentGuide, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Guía agregada a favoritos", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("GuideAdapterView", "Error al agregar guía a favoritos", task.getException());
                            Toast.makeText(context, "Error al agregar guía a favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    //Clase estatica...
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        LinearLayout images;
        ImageButton favs;
        CardView recycler;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            recycler = itemView.findViewById(R.id.recyclerCardGuide);
            name = itemView.findViewById(R.id.textViewName);
            description = itemView.findViewById(R.id.textViewDescription);
            images=itemView.findViewById(R.id.placeView);
            favs=itemView.findViewById(R.id.imageViewFavorite);
        }

        public void bindImages(List<String> imagenes) {

            for (String imageUrl : imagenes) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                imageView.setLayoutParams(imageViewParams);
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                // Cargar la imagen usando Picasso
                Picasso.get().load(imageUrl).resize(1100, 750).into(imageView);

                images.addView(imageView);
            }
        }
    }
}

