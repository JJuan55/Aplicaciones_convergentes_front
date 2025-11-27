package com.example.encomineda20.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.encomineda20.modelo.Cliente;

@Dao
public interface ClienteDao {
    @Insert
    void insertar(Cliente cliente);

    @Query("SELECT * FROM Cliente WHERE Cliente.cli_cedula = :cedula LIMIT 1")
    LiveData<Cliente> obtenerClientePorCedula(String cedula);

    @Query("SELECT * FROM Cliente WHERE Cliente.cli_cedula  = :cedula AND Cliente.cli_clave = :clave LIMIT 1")
    Cliente iniciarSesion(String cedula, String clave);
}
