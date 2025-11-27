package com.example.encomineda20.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.encomineda20.modelo.Asignador;

import java.util.List;

@Dao
public interface AsignadorDao {

    @Insert
    void insertar(Asignador asignador);

    @Query("SELECT * FROM asignador WHERE cedula = :cedula AND clave = :clave LIMIT 1")
    Asignador login(String cedula, String clave);

    @Query("SELECT * FROM asignador")
    List<Asignador> obtenerTodos();

    @Query("SELECT * FROM asignador WHERE cedula = :cedula LIMIT 1")
    Asignador buscarPorCedula(String cedula);

    @Query("DELETE FROM asignador")
    void eliminarTodos();

}

