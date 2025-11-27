package com.example.encomineda20.repositorios;


import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.encomineda20.dao.EncomiendaDao;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Encomienda;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EncomiendaRepositorio {

    private final EncomiendaDao encomiendaDao;
    private final ExecutorService executorService;

    private  AppDatabase db;

    public EncomiendaRepositorio(Application application) {
        db = Room.databaseBuilder(application, AppDatabase.class, "logistica-db")
                .fallbackToDestructiveMigration()
                .build();
        encomiendaDao = db.encomiendaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public Future<Long> insertar(Encomienda encomienda) {
        return executorService.submit(() -> encomiendaDao.insertar(encomienda));
    }
    public LiveData<List<Encomienda>> obtenerEncomiendasDeCliente(String cedulaCliente) {
        return encomiendaDao.obtenerEncomiendasDeCliente(cedulaCliente);
    }

    public LiveData<List<Encomienda>> obtenerTodas() {
        return encomiendaDao.obtenerTodas();
    }

    public void actualizarEstado(int idEncomienda, String nuevoEstado) {
        executorService.execute(() -> {
            encomiendaDao.actualizarEstado(idEncomienda, nuevoEstado);
        });
    }

    public LiveData<List<Encomienda>> obtenerPorCedulaRemitente(String cedula) {
        return encomiendaDao.obtenerEncomiendasDeCliente(cedula);
    }

    public LiveData<List<Encomienda>> getPendientesDePago() {
        return encomiendaDao.getPendientesDePago();
    }

    public LiveData<List<Encomienda>> obtenerNoAsignadas() {
        return encomiendaDao.obtenerNoAsignadas();
    }
    public void pagarEncomienda(int idEncomienda, String metodoPago) {
        executorService.execute(() -> {
            Encomienda encomienda = encomiendaDao.obtenerPorId(idEncomienda);
            if (encomienda != null) {
                encomienda.setPagado(true);
                encomienda.setMetodoPago(metodoPago);
                encomiendaDao.actualizar(encomienda);
            }
        });
    }
    public LiveData<List<Encomienda>> obtenerPorCedulaCliente(String cedula) {
        return encomiendaDao.obtenerEncomiendasDeCliente(cedula);
    }

    public void asignarEncomiendaARepartidor(int idEncomienda, String cedulaRepartidor) {
        executorService.execute(() -> {
            encomiendaDao.asignarEncomiendaARepartidor(idEncomienda, cedulaRepartidor);
        });
    }

    public LiveData<List<Encomienda>> obtenerPorRepartidor(String cedula) {
        MutableLiveData<List<Encomienda>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Encomienda> lista = db.encomiendaDao().getEncomiendasPorRepartidor(cedula);
            liveData.postValue(lista);
        });
        return liveData;
    }
}

