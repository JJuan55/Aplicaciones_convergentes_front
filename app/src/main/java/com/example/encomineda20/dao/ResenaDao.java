package com.example.encomineda20.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.encomineda20.modelo.Resena;
import com.example.encomineda20.modelo.ResenaConCliente;

import java.util.List;

@Dao
public interface ResenaDao {


    @Query("SELECT r.*, c.cli_nom AS nombreCliente " +
            "FROM resenas r " +
            "INNER JOIN Cliente c ON r.cedula_cliente = c.cli_cedula " +
            "WHERE r.cedula_repartidor = :cedulaRepartidor " +
            "ORDER BY r.fecha DESC")
    List<ResenaConCliente> obtenerResenasConCliente(String cedulaRepartidor);

    @Insert
    void insertar(Resena resena);

    @Query("SELECT * FROM resenas WHERE cedula_repartidor = :cedulaRepartidor ORDER BY fecha DESC")
    List<Resena> obtenerReseñasPorRepartidor(String cedulaRepartidor);

    @Query("SELECT AVG(calificacion) FROM resenas WHERE cedula_repartidor = :cedulaRepartidor")
    float obtenerPromedioRepartidor(String cedulaRepartidor);

    @Query("SELECT COUNT(*) FROM resenas WHERE cedula_repartidor = :cedulaRepartidor")
    int contarReseñasRepartidor(String cedulaRepartidor);

    @Query("SELECT * FROM resenas WHERE id_encomienda = :idEncomienda LIMIT 1")
    Resena obtenerPorEncomienda(int idEncomienda);

}

