package com.example.encomineda20.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.encomineda20.modelo.Evaluacion;

import java.util.List;

@Dao
public interface EvaluacionDao {

    @Insert
    void insertar(Evaluacion evaluacion);

    @Query("SELECT * FROM evaluaciones WHERE idEncomienda = :idEncomienda LIMIT 1")
    LiveData<Evaluacion> obtenerPorEncomienda(int idEncomienda);
    @Query("SELECT * FROM evaluaciones ORDER BY fecha DESC")
    LiveData<List<Evaluacion>> obtenerTodas();
}
