package com.example.encomineda20.repositorios;

import android.util.Log;

import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaRequestDTO;
import com.example.encomineda20.dto.encomienda.EncomiendaResponse;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.network.EncomiendaApiService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Callback;

public class EncomiendaBackendRepositorio {

    private final EncomiendaApiService apiService;

    public EncomiendaBackendRepositorio() {
        apiService = ApiClient.getInstance().create(EncomiendaApiService.class);
    }

    public void crearEncomienda(
            EncomiendaRequestDTO dto,
            Callback<EncomiendaResponse> callback
    ) {
        Log.e("Repo", "Enviando DTO: " + new Gson().toJson(dto));
        apiService.crearEncomienda(dto).enqueue(callback);
    }

    public void obtenerPendientes(String cedula, Callback<List<Encomienda>> callback) {
        Log.e("Repo", "Buscando encomiendas pendientes de: " + cedula);
        apiService.obtenerPendientesPorCliente(cedula).enqueue(callback);
    }

    public void eliminarEncomienda(int id, Callback<Void> callback) {
        Log.e("Repo", "Eliminando encomienda id = " + id);
        apiService.eliminarEncomienda(id).enqueue(callback);
    }

    public void pagarEncomienda(int id, String metodoPago, Callback<EncomiendaResponse> callback) {
        Log.e("Repo", "Pagando encomienda " + id + " con " + metodoPago);
        apiService.pagarEncomienda(id, metodoPago).enqueue(callback);
    }

    public void obtenerHistorial(String cedula, Callback<List<Encomienda>> callback) {
        apiService.obtenerHistorial(cedula).enqueue(callback);
    }

}
