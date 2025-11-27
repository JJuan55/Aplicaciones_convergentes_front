package com.example.encomineda20.repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.encomineda20.dao.ClienteDao;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Cliente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClienteRepositorio {

    private ClienteDao clienteDao;
    private ExecutorService executorService;

    public ClienteRepositorio(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        clienteDao = db.clienteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertar(Cliente cliente) {
        executorService.execute(() -> clienteDao.insertar(cliente));
    }

    public LiveData<Cliente> obtenerCliente(String cedula) {
        return clienteDao.obtenerClientePorCedula(cedula);
    }

    public interface LoginCallback {
        void onResultado(Cliente cliente);
    }

    public void iniciarSesion(String cedula, String clave, LoginCallback callback) {
        executorService.execute(() -> {
            Cliente cliente = clienteDao.iniciarSesion(cedula, clave);
            callback.onResultado(cliente);
        });
    }
}
