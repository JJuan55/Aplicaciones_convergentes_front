package com.example.encomineda20.repositorios;



import android.content.Context;

import com.example.encomineda20.dao.AsignadorDao;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Asignador;

import java.util.List;

public class AsignadorRepositorio {

    private AsignadorDao asignadorDao;

    public AsignadorRepositorio(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context); //analizar
        asignadorDao = db.asignadorDao();
    }

    public void insertar(Asignador asignador) {
        new Thread(() -> asignadorDao.insertar(asignador)).start();
    }

    public Asignador login(String cedula, String clave) {
        return asignadorDao.login(cedula, clave);
    }

    public List<Asignador> obtenerTodos() {
        return asignadorDao.obtenerTodos();
    }
}

