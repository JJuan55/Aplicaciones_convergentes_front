package com.example.encomineda20.repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.encomineda20.dao.EvaluacionDao;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Evaluacion;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvaluacionRepositorio {
    private final EvaluacionDao evaluacionDao;
    private final ExecutorService executorService;

    public EvaluacionRepositorio(Application application) {
        AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "app_db").fallbackToDestructiveMigration().build();
        evaluacionDao = db.evaluacionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertar(Evaluacion evaluacion) {
        executorService.execute(() -> evaluacionDao.insertar(evaluacion));
    }

    public LiveData<Evaluacion> obtenerPorEncomienda(int idEncomienda) {
        return evaluacionDao.obtenerPorEncomienda(idEncomienda);
    }

    public LiveData<List<Evaluacion>> obtenerTodas() {
        return evaluacionDao.obtenerTodas();
    }
}
