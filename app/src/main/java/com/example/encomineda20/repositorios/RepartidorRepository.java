package com.example.encomineda20.repositorios;



import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.encomineda20.dao.RepartidorDao;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Repartidor;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepartidorRepository {

    private final RepartidorDao repartidorDao;
    private final ExecutorService executorService;

    public RepartidorRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        repartidorDao = db.repartidorDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Repartidor>> obtenerTodos() {
        MutableLiveData<List<Repartidor>> data = new MutableLiveData<>();
        executorService.execute(() -> {
            List<Repartidor> lista = repartidorDao.getAllRepartidores();
            data.postValue(lista);
        });
        return data;
    }
}
