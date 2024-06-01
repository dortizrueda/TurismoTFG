package com.example.turismotfg.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Entity.Places;
import com.example.turismotfg.Activities.PlaceProfile;
import com.example.turismotfg.R;

import java.util.List;
/**
 * Clase que muestra e adaptor para muetra el listado de lugares.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    private Context context;
    private List<Places> placeList;
    /**
     * Constructor del adaptador de lugares.
     * @param context El contexto de la actividad.
     * @param placeList La lista de lugares a mostrar.
     */
    public PlaceAdapter(Context context, List<Places> placeList) {
        this.context = context;
        this.placeList = placeList;
    }
    /**
     * Método que crea nuevas vistas.
     * @param parent El ViewGroup al cual estas nuevas vistas serán añadidas después de ser vinculadas a una posición del adaptador.
     * @param viewType El tipo de la nueva vista.
     * @return Un nuevo ViewHolder que contiene una vista del lugar.
     */
    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceAdapter.ViewHolder(view, context);
    }
    /**
     * Método se encarga de vincular los datos de los lugares.
     *
     * @param holder referencias a las vistas de los adaptadores.
     * @param position Posición de la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        holder.name.setText(placeList.get(position).getNombre());
        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.context, PlaceProfile.class);
                Places place = placeList.get(position);
                intent.putExtra("place",place);
                holder.context.startActivity(intent);
            }
        });
    }
    /**
     * Devuelve el numero de elementos del recyclerView.
     * @return  int Devuelve el numero de items de la lista.
     */
    @Override
    public int getItemCount() {return placeList.size();}
    /**
     * Clase estática de ViewHolder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
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
            name = itemView.findViewById(R.id.textViewName);
            recycler = itemView.findViewById(R.id.recyclerCardPlace);
        }
    }
}
