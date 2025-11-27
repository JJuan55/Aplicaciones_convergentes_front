package com.example.encomineda20.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.modelo.Encomienda;

import java.util.ArrayList;
import java.util.List;

public class ItemNotificacionAdapter extends RecyclerView.Adapter<ItemNotificacionAdapter.NotifViewHolder> {

    private final List<EncomiendaDTO> lista;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onCalificar(EncomiendaDTO encomienda, int position);
    }

    public ItemNotificacionAdapter(List<EncomiendaDTO> lista, OnItemActionListener listener) {
        this.lista = (lista != null) ? lista : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notificacion, parent, false);
        return new NotifViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        EncomiendaDTO encomienda = lista.get(position);

        holder.txtMensaje.setText("Encomienda #" + encomienda.getId() + " entregada");
        holder.btnCalificar.setOnClickListener(v -> listener.onCalificar(encomienda, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class NotifViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensaje;
        Button btnCalificar;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensaje = itemView.findViewById(R.id.txtMensaje);
            btnCalificar = itemView.findViewById(R.id.btnCalificar);
        }
    }
}



