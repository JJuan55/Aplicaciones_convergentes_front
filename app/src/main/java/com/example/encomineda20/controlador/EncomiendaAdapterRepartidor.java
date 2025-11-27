package com.example.encomineda20.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;

import java.util.List;

public class EncomiendaAdapterRepartidor extends RecyclerView.Adapter<EncomiendaAdapterRepartidor.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(EncomiendaDTO encomienda);
    }

    private List<EncomiendaDTO> lista;
    private OnItemClickListener listener;

    public EncomiendaAdapterRepartidor(List<EncomiendaDTO> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<EncomiendaDTO> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_encomienda_asignada, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EncomiendaDTO e = lista.get(position);
        holder.tvDestino.setText(e.getDestino());
        holder.tvCliente.setText(e.getNombreCliente());
        holder.tvEstado.setText(e.getEstado());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(e));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestino, tvCliente, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestino = itemView.findViewById(R.id.tvDestino);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}


