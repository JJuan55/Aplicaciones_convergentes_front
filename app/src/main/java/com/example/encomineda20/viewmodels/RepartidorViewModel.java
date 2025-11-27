package com.example.encomineda20.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.modelo.Repartidor;

import java.util.List;

public class RepartidorViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public RepartidorViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Repartidor>> obtenerTodos() {
        MutableLiveData<List<Repartidor>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Repartidor> lista = db.repartidorDao().getAllRepartidores();
            liveData.postValue(lista);
        });
        return liveData;
    }
}
