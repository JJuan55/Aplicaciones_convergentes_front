package com.example.encomineda20.viewmodels;



import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.asignador.AsignarEncomiendaRequest;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.network.AsignadorApi;
import com.example.encomineda20.repositorios.EncomiendaRepositorio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncomiendaViewModel extends ViewModel {

    private AsignadorApi api = ApiClient.getInstance().create(AsignadorApi.class);
    private MutableLiveData<Boolean> resultadoAsignacion = new MutableLiveData<>();

    public LiveData<Boolean> getResultadoAsignacion() {
        return resultadoAsignacion;
    }

    public void asignarEncomiendaARepartidor(int idEncomienda, String cedula) {

        Log.d("ENCOMIENDA_VM", "➡️ Enviando asignación: " + idEncomienda + " -> " + cedula);

        api.asignar(idEncomienda, cedula).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                boolean ok = response.isSuccessful();
                Log.d("ENCOMIENDA_VM", "✔️ Respuesta asignación: " + ok);
                resultadoAsignacion.postValue(ok);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ENCOMIENDA_VM", "Error asignando: " + t.getMessage());
                resultadoAsignacion.postValue(false);
            }
        });
    }
}

