package com.example.encomineda20.controlador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.repartidor.ResenaDTO;
import com.example.encomineda20.modelo.Resena;
import com.example.encomineda20.modelo.ResenaConCliente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResenaAdapter extends BaseAdapter {

    private Context context;
    private List<ResenaDTO> listaResenas;

    public ResenaAdapter(Context context, List<ResenaDTO> listaResenas) {
        this.context = context;
        this.listaResenas = listaResenas;
    }

    @Override
    public int getCount() {
        return listaResenas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaResenas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaResenas.get(position).getId();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_resena, parent, false);

        TextView tvCliente = view.findViewById(R.id.tvCliente);
        RatingBar ratingBar = view.findViewById(R.id.ratingBarItem);
        TextView tvComentario = view.findViewById(R.id.tvComentario);
        TextView tvFecha = view.findViewById(R.id.tvFecha);

        ResenaDTO resena = listaResenas.get(position);

        tvCliente.setText("Cliente: " + resena.getNombreCliente());
        ratingBar.setRating(resena.getCalificacion().floatValue());
        tvComentario.setText(resena.getComentario() == null || resena.getComentario().isEmpty() ? "Sin comentario" : resena.getComentario());

        // Convertir string ISO a formato dd/MM/yyyy
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = sdfInput.parse(resena.getFecha());
            String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
            tvFecha.setText("Fecha: " + fechaFormateada);
        } catch (ParseException e) {
            tvFecha.setText("Fecha: -");
        }

        return view;
    }
}

