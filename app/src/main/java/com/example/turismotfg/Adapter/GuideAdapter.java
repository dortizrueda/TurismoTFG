package com.example.turismotfg.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Activities.GuideProfile;
import com.example.turismotfg.Managers.guideManager;
import com.example.turismotfg.Managers.userManager;
import com.example.turismotfg.Entity.Guide;
import com.example.turismotfg.Entity.Places;
import com.example.turismotfg.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.squareup.picasso.Picasso;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase que muestra e adaptor para muestrar el listado de guías.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {
    final userManager userManager;
    guideManager guideManager;
    private Context context;
    private List<Guide> guideList;
    private List<Places> placeList;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    /**
     * Constructor del adaptador de guías.
     * @param context El contexto de la actividad.
     * @param guideList La lista de guías a mostrar.
     */
    public GuideAdapter(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
        this.userManager = new userManager(context);
        this.guideManager =new guideManager(context);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    /**
     * Método que crea nuevas vistas.
     * @param parent El ViewGroup al cual estas nuevas vistas serán añadidas después de ser vinculadas a una posición del adaptador.
     * @param viewType El tipo de la nueva vista.
     * @return Un nuevo ViewHolder que contiene una vista de la guía.
     */
    @NonNull
    @Override
    public GuideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false);
        return new GuideAdapter.ViewHolder(view, context);
    }

    /**
     * Método se encarga de vincular los datos de las guías.
     *
     * @param holder referencias a las vistas de los adaptadores.
     * @param position Posición de la lista.
     */

    @Override
    public void onBindViewHolder(@NonNull GuideAdapter.ViewHolder holder, int position) {
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
        //Envio de información al GuideProfile
        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.context, GuideProfile.class);
                i.putExtra("name", guideList.get(holder.getAdapterPosition()).getName());
                i.putExtra("description", guideList.get(holder.getAdapterPosition()).getDescription());
                i.putExtra("creator",guideList.get(holder.getAdapterPosition()).getCreator());
                i.putExtra("audioURL",guideList.get(holder.getAdapterPosition()).getAudioUrl());
                i.putExtra("placeList",(Serializable) placeList );
                holder.context.startActivity(i);
            }
        });


    }
    /**
     * Devuelve el numero de elementos del recyclerView.
     * @return  int Devuelve el numero de items de la lista.
     */
    @Override
    public int getItemCount() {
        return Math.min(2, guideList.size());
    }



    //Clase estatica...
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        LinearLayout images;
        RatingBar mediabar;
        ImageButton favs;
        CardView recycler;
        Context context;
        /**
         * Constructor de ViewHolder.
         *
         * @param itemView Vista del layout.
         * @param context Contexto de la actividad.
         */
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            recycler = itemView.findViewById(R.id.recyclerCardGuide);
            name = itemView.findViewById(R.id.textViewName);
            description = itemView.findViewById(R.id.textViewDescription);
            images=itemView.findViewById(R.id.placeView);

        }
        /**
         * Vincula las imágenes del lugar a la vista.
         * @param imagenes Lista de URLs de las imágenes.
         */
        public void bindImages(List<String> imagenes) {
            images.removeAllViewsInLayout();
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

