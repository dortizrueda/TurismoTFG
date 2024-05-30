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
import com.example.turismotfg.Managers.valoracionesManager;
import com.example.turismotfg.Entity.Guide;
import com.example.turismotfg.Entity.Places;
import com.example.turismotfg.Managers.userManager;
import com.example.turismotfg.Managers.guideManager;

import com.example.turismotfg.Entity.Valoration;
import com.example.turismotfg.R;
import com.example.turismotfg.interfaces.GuideFavCallBack;
import com.example.turismotfg.interfaces.ValorationList;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuideAdapterView extends RecyclerView.Adapter<GuideAdapterView.ViewHolder> {

    final userManager userManager;
    guideManager guideManager;
    private Context context;
    private List<Guide> guideList;
    private List<Places> placeList;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public GuideAdapterView(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
        this.userManager = new userManager(context);
        this.guideManager =new guideManager(context);
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
        guideManager.checkGuide(user.getUid(), guideList.get(position).getName(), new GuideFavCallBack() {
            @Override
            public void isFav(boolean fav) {
                if (fav){
                    holder.favs.setBackgroundResource(R.drawable.ic_favs_filled);
                }else{
                    holder.favs.setBackgroundResource(R.drawable.ic_favs);
                }
            }
            @Override
            public void onError(Exception exception) {

            }
        });
        valoracionesManager.getAllValByGuide(guideList.get(position).getName(), new ValorationList() {
            @Override
            public void onListValoration(List<Valoration> valoraciones) {
                int contador=0;
                float suma=0;
                for (Valoration v:valoraciones) {
                    Log.d("MEDIA", String.valueOf(v.getGuideId()));
                    suma = suma + v.getRating();
                    contador = contador + 1;
                }
                float valoracion_media=suma/contador;
                Log.d("MEDIA",String.valueOf(valoracion_media));
                holder.mediabar.setRating(valoracion_media);
            }

            @Override
            public void onError(Exception exception) {

            }
        });


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
                i.putExtra("creator",guideList.get(holder.getAdapterPosition()).getCreator());
                i.putExtra("audioURL",guideList.get(holder.getAdapterPosition()).getAudioUrl());
                i.putExtra("placeList",(Serializable) placeList );
                holder.context.startActivity(i);
            }
        });

        holder.favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guide currentGuide = guideList.get(holder.getAdapterPosition());
                guideManager.checkGuide(user.getUid(), currentGuide.getName(), new GuideFavCallBack() {
                    @Override
                    public void isFav(boolean fav) {
                        if (fav){
                            userManager.removeGuideFromFavs(currentGuide,new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context,"Guia eliminada de favs",Toast.LENGTH_SHORT).show();
                                        holder.favs.setBackgroundResource(R.drawable.ic_favs);
                                    }else{
                                        Toast.makeText(context,"Error al eliminar de favs",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            userManager.addGuideToUserFavs(currentGuide, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Guía agregada a favoritos", Toast.LENGTH_SHORT).show();
                                        holder.favs.setBackgroundResource(R.drawable.ic_favs_filled);

                                    } else {
                                        Log.e("GuideAdapterView", "Error al agregar guía a favoritos", task.getException());
                                        Toast.makeText(context, "Error al agregar guía a favoritos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onError(Exception exception) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }
    public void getInOrderByMedia(){
        Log.d("DENTRO", "getInOrderByMedia");
        for (Guide g:guideList){
            getMediaValoration(g);
        }
    }
    public void setGuideListFilter(List<Guide>list){
        this.guideList=list;
        notifyDataSetChanged();
    }
    private void getMediaValoration(Guide o2) {
        valoracionesManager.getAllValByGuide(o2.getName(), new ValorationList() {
            @Override
            public void onListValoration(List<Valoration> valoraciones) {
                int contador=0;
                float suma=0;
                for (Valoration v:valoraciones) {
                    Log.d("MEDIA", String.valueOf(v.getGuideId()));
                    suma = suma + v.getRating();
                    contador = contador + 1;
                }
                float valoracion_media=suma/contador;
                o2.setMedia(valoracion_media);
                Log.d("MEDIA",String.valueOf(valoracion_media));
                if (guideList.indexOf(o2) == guideList.size() - 1) {
                    Collections.sort(guideList, new Comparator<Guide>() {
                        @Override
                        public int compare(Guide o1, Guide o2) {
                            return Float.compare(o2.getMedia(), o1.getMedia()); // Orden descendente
                        }
                    });
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    public void getInOrderByMediaAsc() {
        Log.d("DENTRO", "getInOrderByMedia");
        for (Guide g:guideList){
            getMediaValoration2(g);
        }
    }

    private void getMediaValoration2(Guide g) {
        valoracionesManager.getAllValByGuide(g.getName(), new ValorationList() {
            @Override
            public void onListValoration(List<Valoration> valoraciones) {
                int contador=0;
                float suma=0;
                for (Valoration v:valoraciones) {
                    Log.d("MEDIA", String.valueOf(v.getGuideId()));
                    suma = suma + v.getRating();
                    contador = contador + 1;
                }
                float valoracion_media=suma/contador;
                g.setMedia(valoracion_media);
                Log.d("MEDIA",String.valueOf(valoracion_media));
                if (guideList.indexOf(g) == guideList.size() - 1) {
                    Collections.sort(guideList, new Comparator<Guide>() {
                        @Override
                        public int compare(Guide o1, Guide o2) {
                            return Float.compare(o1.getMedia(), o2.getMedia()); // Orden descendente
                        }
                    });
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    //Clase estatica...
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        LinearLayout images;
        RatingBar mediabar;
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
            mediabar=itemView.findViewById(R.id.average_rating);
        }

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

