package com.example.encomineda20.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.encomineda20.modelo.Repartidor;

import java.util.List;

/**
 * DAO de Repartidor: define las operaciones CRUD básicas
 * para registro, autenticación y consulta.
 */
@Dao
public interface RepartidorDao {

        // Inserta un nuevo repartidor, falla si la cédula ya existe
        @Insert(onConflict = OnConflictStrategy.ABORT)
        void insertRepartidor(Repartidor repartidor);

        // Consulta por cédula
        @Query("SELECT * FROM repartidor WHERE cedula = :cedula LIMIT 1")
        Repartidor getRepartidorByCedula(String cedula);

        // Consulta por correo
        @Query("SELECT * FROM repartidor WHERE correo = :correo LIMIT 1")
        Repartidor getRepartidorByCorreo(String correo);

        // Login por cédula y clave
        @Query("SELECT * FROM repartidor WHERE cedula = :cedula AND clave = :clave LIMIT 1")
        Repartidor loginRepartidor(String cedula, String clave);

        // Lista todos los repartidores
        @Query("SELECT * FROM repartidor")
        List<Repartidor> getAllRepartidores();

        @Query("SELECT * FROM repartidor WHERE cedula = :cedula LIMIT 1")
        Repartidor obtenerPorCedula(String cedula);

        @Update
        void actualizar(Repartidor repartidor);

    }


