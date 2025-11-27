package com.example.encomineda20.controlador;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.encomineda20.modelo.Evaluacion;
import com.example.encomineda20.repositorios.EvaluacionRepositorio;

public class EvaluacionControlador {
    private final EvaluacionRepositorio repositorio;

    public EvaluacionControlador(Application application) {
        repositorio = new EvaluacionRepositorio(application);
    }

    public void registrarEvaluacion(Evaluacion evaluacion) {
        repositorio.insertar(evaluacion);
    }

    public LiveData<Evaluacion> obtenerEvaluacionPorEncomienda(int idEncomienda) {
        return repositorio.obtenerPorEncomienda(idEncomienda);
    }
}
