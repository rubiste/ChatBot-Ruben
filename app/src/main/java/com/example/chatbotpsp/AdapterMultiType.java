package com.example.chatbotpsp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMultiType extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_USER = 1;
    private static int TYPE_BOT = 2;
    private Context context;
    public ArrayList<Mensaje> mensajes;

    public AdapterMultiType(Context context) {
        this.context = context;
        this.mensajes = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == TYPE_USER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false);
            return new UserViewHolder(view);

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_bot, viewGroup, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mensajes.get(position).persona) {
            return TYPE_USER;
        } else {
            return TYPE_BOT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_USER) {
            ((UserViewHolder) viewHolder).setMensajeDetails(mensajes.get(position));
        } else {
            ((BotViewHolder) viewHolder).setMensajeDetails(mensajes.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMensaje;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvHumanMessage);
        }

        void setMensajeDetails(Mensaje mensaje) {
            tvMensaje.setText(mensaje.mensaje);
        }
    }

    class BotViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMensaje;

        BotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvBotMessage);
        }

        void setMensajeDetails(Mensaje mensaje) {
            tvMensaje.setText(mensaje.mensaje);
        }
    }

}
