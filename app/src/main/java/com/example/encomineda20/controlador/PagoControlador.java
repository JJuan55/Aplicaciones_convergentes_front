package com.example.encomineda20.controlador;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.encomineda20.repositorios.EncomiendaRepositorio;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Encomienda;

import java.util.List;

/**
 *
 */
public class PagoControlador extends AndroidViewModel {
    private final EncomiendaRepositorio repo;
    private final LiveData<List<Encomienda>> pendientes;

    public PagoControlador(@NonNull Application app) {
        super(app);
        AppDatabase db = AppDatabase.getDatabase(app);
        repo = new EncomiendaRepositorio((Application) db.encomiendaDao());
        pendientes = repo.getPendientesDePago();
    }

    public LiveData<List<Encomienda>> getPendientes() {
        return pendientes;
    }

    public void pagar(int id, String metodo) {
        repo.pagarEncomienda(id, metodo);
    }
}
