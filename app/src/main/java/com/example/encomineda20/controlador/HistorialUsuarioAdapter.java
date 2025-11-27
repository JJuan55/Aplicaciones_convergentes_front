package com.example.encomineda20.controlador;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.modelo.Encomienda;

import java.util.List;

public class HistorialUsuarioAdapter extends RecyclerView.Adapter<HistorialUsuarioAdapter.ViewHolder> {

    private List<Encomienda> lista;

    public HistorialUsuarioAdapter(List<Encomienda> lista) {
        this.lista = lista;
    }

    public void setLista(List<Encomienda> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Encomienda e = lista.get(position);

        holder.tvTipoProducto.setText("Producto: " + e.getTipoProducto());
        holder.tvRuta.setText(e.getOrigen() + " → " + e.getDestino());
        holder.tvPrecio.setText("Precio: $" + (int) e.getPrecio());
        holder.tvFecha.setText("Fecha: " + e.getFechaSolicitud());
        holder.tvEstado.setText(e.getEstado());

        // Color según el estado
        if ("Entregada".equalsIgnoreCase(e.getEstado())) {
            holder.tvEstado.setTextColor(Color.parseColor("#2E7D32")); // Verde
        } else {
            holder.tvEstado.setTextColor(Color.parseColor("#B71C1C")); // Rojo
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTipoProducto, tvRuta, tvPrecio, tvFecha, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTipoProducto = itemView.findViewById(R.id.tvTipoProducto);
            tvRuta = itemView.findViewById(R.id.tvRuta);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}

