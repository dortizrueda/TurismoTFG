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

import com.example.turismotfg.Entity.Places;

import java.io.Serializable;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    private Context context;
    private List<Places> placeList;

    public PlaceAdapter(Context context, List<Places> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        holder.name.setText(placeList.get(position).getNombre());
        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.context,PlaceProfile.class);
                Places place = placeList.get(position);
                intent.putExtra("place",place);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return placeList.size();}
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        CardView recycler;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            name = itemView.findViewById(R.id.textViewName);
            recycler = itemView.findViewById(R.id.recyclerCardPlace);
        }
    }
}
