package com.example.encomineda20.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.modelo.Encomienda;

import java.util.List;

public class EncomiendaAdapter extends RecyclerView.Adapter<EncomiendaAdapter.ViewHolder> {

    private List<Encomienda> lista;
    private OnEncomiendaClickListener listener;

    public interface OnEncomiendaClickListener {
        void onCancelarClick(Encomienda encomienda);
    }

    public EncomiendaAdapter(List<Encomienda> lista, OnEncomiendaClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_encomienda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Encomienda e = lista.get(position);

        holder.tvDestino.setText("Destino: " + e.getDestino());
        holder.tvTipoValor.setText(e.getTipoProducto() + " - $" + e.getPrecio());
        holder.tvEstado.setText("Estado: " + e.getEstado());

        if (!"Pendiente".equalsIgnoreCase(e.getEstado())) {
            holder.btnCancelar.setVisibility(View.GONE);
        } else {
            holder.btnCancelar.setVisibility(View.VISIBLE);
        }

        holder.btnCancelar.setOnClickListener(v -> listener.onCancelarClick(e));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDestino, tvTipoValor, tvEstado;
        Button btnCancelar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestino = itemView.findViewById(R.id.tvDestino);
            tvTipoValor = itemView.findViewById(R.id.tvTipoValor);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }
    }
}




