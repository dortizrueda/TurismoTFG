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

import com.example.turismotfg.Entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Asignamos la vista para mostrar el diseño del elemento
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.email.setText(userList.get(position).getEmail());

        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, UserProfile.class);
                intent.putExtra("email", userList.get(holder.getAdapterPosition()).getEmail());
                intent.putExtra("name", userList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("surname",userList.get(holder.getAdapterPosition()).getSurname());
                intent.putExtra("rol",userList.get(holder.getAdapterPosition()).getRol().toString());
                holder.context.startActivity(intent);
            }
        });
    }

    //Devuelve el número total de usuarios.
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView email,name;
        CardView recycler;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            email = itemView.findViewById(R.id.textViewEmail);
            name=itemView.findViewById(R.id.textViewName);
            recycler = itemView.findViewById(R.id.recyclerCardUser);
        }
    }
}

