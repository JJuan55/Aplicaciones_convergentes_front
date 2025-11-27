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

public class EncomiendaAdapterAsignador extends RecyclerView.Adapter<EncomiendaAdapterAsignador.ViewHolder> {

    private List<EncomiendaDTO> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(EncomiendaDTO encomienda);
    }

    public EncomiendaAdapterAsignador(List<EncomiendaDTO> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<EncomiendaDTO> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_encomienda_no_asignada, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EncomiendaDTO e = lista.get(position);

        holder.tvTipoProducto.setText(e.getTipoProducto());
        holder.tvOrigenDestino.setText("De: " + e.getOrigen() + " → A: " + e.getDestino());
        holder.tvEstado.setText("Estado: " + e.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(e);
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTipoProducto, tvOrigenDestino, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoProducto = itemView.findViewById(R.id.tvTipoProducto);
            tvOrigenDestino = itemView.findViewById(R.id.tvOrigenDestino);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}

