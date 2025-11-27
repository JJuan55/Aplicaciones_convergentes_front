package com.example.encomineda20.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.encomineda20.modelo.HistorialRepartidor;

import java.util.List;

@Dao
public interface HistorialRepartidorDao {
    @Insert
    void insertar(HistorialRepartidor historial);

    @Query("SELECT * FROM historial_repartidor WHERE cedulaRepartidor = :cedula ORDER BY fechaEntrega DESC")
    List<HistorialRepartidor> obtenerPorRepartidor(String cedula);

    @Query("SELECT * FROM historial_repartidor WHERE cedulaRepartidor = :cedula ORDER BY fechaEntrega DESC LIMIT :limite OFFSET :inicio")
    List<HistorialRepartidor> obtenerPorRepartidorPaginado(String cedula, int inicio, int limite);

    @Query("SELECT COUNT(*) FROM historial_repartidor WHERE cedulaRepartidor = :cedula")
    int contarEntregas(String cedula);

    @Query("SELECT SUM(valorDeclarado) FROM historial_repartidor WHERE cedulaRepartidor = :cedula")
    Double sumarDineroRecaudado(String cedula);

}
