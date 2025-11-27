package com.example.encomineda20.controlador;



import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.repartidor.RepartidorDTO;
import com.example.encomineda20.modelo.Repartidor;

import java.util.ArrayList;
import java.util.List;

public class RepartidorAdapter extends RecyclerView.Adapter<RepartidorAdapter.ViewHolder> {

    private List<RepartidorDTO> lista;
    private final OnItemClickListener listener;
    private RepartidorDTO repartidorSeleccionado = null;

    public interface OnItemClickListener {
        void onItemClick(RepartidorDTO repartidor);
    }

    public RepartidorAdapter(List<RepartidorDTO> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<RepartidorDTO> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repartidor_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepartidorDTO r = lista.get(position);
        holder.tvNombre.setText(r.getNombre());
        holder.tvTipoVehiculo.setText("Vehículo: " + r.getTipoVehiculo());
        holder.tvModeloMatricula.setText("Modelo: " + r.getModelo() + " | Matrícula: " + r.getMatricula());

        // Cambiar fondo si está seleccionado
        if (r.equals(repartidorSeleccionado)) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF3E0")); // color de resaltado
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        // Click en el botón seleccionar
        holder.btnSeleccionar.setOnClickListener(v -> {
            repartidorSeleccionado = r;
            notifyDataSetChanged(); // refresca para mostrar el resaltado
            listener.onItemClick(r);
        });

        // Click en la card también puede seleccionar
        holder.cardView.setOnClickListener(v -> {
            repartidorSeleccionado = r;
            notifyDataSetChanged();
            listener.onItemClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTipoVehiculo, tvModeloMatricula;
        Button btnSeleccionar;
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvNombre = itemView.findViewById(R.id.tvNombreRepartidor);
            tvTipoVehiculo = itemView.findViewById(R.id.tvTipoVehiculo);
            tvModeloMatricula = itemView.findViewById(R.id.tvModeloMatricula);
            btnSeleccionar = itemView.findViewById(R.id.btnSeleccionarRepartidor);
        }
    }
}




