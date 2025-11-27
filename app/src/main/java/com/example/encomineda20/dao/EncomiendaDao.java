package com.example.encomineda20.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.encomineda20.modelo.Encomienda;

import java.util.List;

@Dao
public interface EncomiendaDao {

    @Insert
    long insertar(Encomienda encomienda); // ← CAMBIO: ahora devuelve el id generado

    @Query("SELECT * FROM encomiendas WHERE cedulaCliente = :cedulaCliente ORDER BY fechaSolicitud DESC")
    LiveData<List<Encomienda>> obtenerEncomiendasDeCliente(String cedulaCliente);

    @Query("SELECT * FROM encomiendas ORDER BY fechaSolicitud DESC")
    LiveData<List<Encomienda>> obtenerTodas();


    @Query("UPDATE encomiendas SET estado = :nuevoEstado WHERE id = :idEncomienda")
    void actualizarEstado(int idEncomienda, String nuevoEstado);

    //  @Query("SELECT * FROM encomiendas WHERE id = :idEncomienda LIMIT 1")
    //LiveData<Encomienda> obtenerPorId(int idEncomienda);

    @Query("SELECT * FROM encomiendas WHERE id = :idEncomienda LIMIT 1")
    Encomienda obtenerPorId(int idEncomienda);

    @Query("UPDATE encomiendas SET pagado = :pagado, metodoPago = :metodo WHERE id = :id")
    void actualizarPago(int id, boolean pagado, String metodo);

    @Query("SELECT * FROM encomiendas WHERE pagado = 0")
    LiveData<List<Encomienda>> getPendientesDePago();

    @Update
    void actualizar(Encomienda encomienda);


    @Query("SELECT * FROM encomiendas WHERE cedulaCliente = :cedula")
    List<Encomienda> getEncomiendasPorCedula(String cedula);


    @Query("SELECT * FROM encomiendas WHERE cedulaRepartidor = :cedula")
    List<Encomienda> obtenerEncomiendasPorRepartidor(String cedula);


    // Asignar encomienda a un repartidor
    @Query("UPDATE encomiendas SET cedulaRepartidor = :cedulaRepartidor, estado = 'asignada' WHERE id = :idEncomienda")
    void asignarEncomiendaARepartidor(int idEncomienda, String cedulaRepartidor);


    @Query("SELECT * FROM encomiendas WHERE cedulaRepartidor IS NULL OR cedulaRepartidor = ''")
    LiveData<List<Encomienda>> obtenerNoAsignadas();

    @Query("SELECT * FROM encomiendas WHERE cedulaRepartidor = :cedula")
    List<Encomienda> getEncomiendasPorRepartidor(String cedula);

    @Delete
    void eliminarEncomienda(Encomienda encomienda);

    @Query("SELECT * FROM Encomiendas WHERE cedulaRepartidor = :cedula AND estado = 'Entregada'")
    List<Encomienda> obtenerEntregadasPorRepartidor(String cedula);

    @Query("SELECT * FROM encomiendas WHERE cedulaRepartidor = :cedula AND estado != 'Entregada'")
    List<Encomienda> obtenerEncomiendasAsignadasNoEntregadas(String cedula);

    @Query("SELECT * FROM encomiendas WHERE cedulaRepartidor = :cedula AND estado != 'Entregada'")
    LiveData<List<Encomienda>> obtenerEncomiendasAsignadasNoEntregadasLive(String cedula);

    @Query("SELECT * FROM encomiendas WHERE cedulaCliente = :cedula AND estado = 'Entregada' AND pendiente_calificacion = 1")
    List<Encomienda> getEncomiendasPendientesCalificacion(String cedula);
}
