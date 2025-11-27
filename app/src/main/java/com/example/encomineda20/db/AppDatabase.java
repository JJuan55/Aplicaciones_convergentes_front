package com.example.encomineda20.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.encomineda20.dao.AsignadorDao;
import com.example.encomineda20.dao.ClienteDao;
import com.example.encomineda20.dao.EncomiendaDao;
import com.example.encomineda20.dao.EvaluacionDao;
import com.example.encomineda20.dao.HistorialRepartidorDao;
import com.example.encomineda20.dao.RepartidorDao;
import com.example.encomineda20.dao.ResenaDao;
import com.example.encomineda20.modelo.Asignador;
import com.example.encomineda20.modelo.Cliente;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.modelo.Evaluacion;
import com.example.encomineda20.modelo.HistorialRepartidor;
import com.example.encomineda20.modelo.Repartidor;
import com.example.encomineda20.modelo.Resena;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                Cliente.class,
                Encomienda.class,
                Evaluacion.class,
                Repartidor.class,
                Asignador.class,
                HistorialRepartidor.class,
                Resena.class
        },
        version = 9,
        exportSchema = false)
//9

public abstract class AppDatabase extends RoomDatabase {
    public abstract ClienteDao clienteDao();
    public abstract EncomiendaDao encomiendaDao();
    public abstract EvaluacionDao evaluacionDao();
    public abstract AsignadorDao asignadorDao();
    public abstract RepartidorDao repartidorDao();
    public abstract HistorialRepartidorDao historialRepartidorDao();
    public abstract ResenaDao reseñaDao();



    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "logistica-db"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
