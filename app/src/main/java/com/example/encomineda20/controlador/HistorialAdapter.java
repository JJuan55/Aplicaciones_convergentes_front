package com.example.encomineda20.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.modelo.HistorialRepartidor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private List<HistorialRepartidor> lista;

    public HistorialAdapter(List<HistorialRepartidor> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial, parent, false); // Usamos layout propio
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistorialRepartidor h = lista.get(position);

        // Formateamos la fecha
        Date fecha = new Date(h.getFechaEntrega());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        holder.tvDestino.setText("Destino: " + h.getDestino());
        holder.tvValor.setText(String.format(Locale.getDefault(), "Valor: $%.2f", h.getValorDeclarado()));
        holder.tvFecha.setText("Fecha: " + formato.format(fecha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestino, tvValor, tvFecha;

        ViewHolder(View v) {
            super(v);
            tvDestino = v.findViewById(R.id.tvDestino);
        //    tvValor = v.findViewById(R.id.tvValor);
            tvFecha = v.findViewById(R.id.tvFecha);
        }
    }
}
