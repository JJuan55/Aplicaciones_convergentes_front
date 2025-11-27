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

public class HistorialRepartidorAdapter extends RecyclerView.Adapter<HistorialRepartidorAdapter.ViewHolder> {

    private List<EncomiendaDTO> lista;

    public HistorialRepartidorAdapter(List<EncomiendaDTO> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvCliente, tvDestinatario, tvDestino, tvPrecio, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvDestinatario = itemView.findViewById(R.id.tvDestinatario);
            tvDestino = itemView.findViewById(R.id.tvDestino);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_repartidor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        EncomiendaDTO e = lista.get(position);

        h.tvDescripcion.setText("Producto: " + e.getDescripcion());
        h.tvCliente.setText("Cliente: " + e.getNombreCliente());
        h.tvDestinatario.setText("Destinatario: " + e.getNombreDestinatario());
        h.tvDestino.setText("Destino: " + e.getDestino());
        h.tvPrecio.setText("Precio: $" + e.getPrecio());
        h.tvFecha.setText("Fecha entrega: " + e.getFechaSolicitud());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void updateList(List<EncomiendaDTO> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

}
