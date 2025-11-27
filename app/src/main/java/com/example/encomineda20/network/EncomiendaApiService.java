package com.example.encomineda20.network;

import com.example.encomineda20.dto.encomienda.EncomiendaRequestDTO;
import com.example.encomineda20.dto.encomienda.EncomiendaResponse;
import com.example.encomineda20.modelo.Encomienda;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EncomiendaApiService {

    @POST("api/encomiendas/registrar")
    Call<EncomiendaResponse> crearEncomienda(@Body EncomiendaRequestDTO dto);

    @PUT("api/encomiendas/{id}/pago")
    Call<EncomiendaResponse> pagarEncomienda(
            @Path("id") int id,
            @Query("metodoPago") String metodoPago
    );

    @GET("api/encomiendas/cliente/{cedula}")
    Call<List<Encomienda>> obtenerPorCliente(@Path("cedula") String cedula);

    @GET("/api/encomiendas/cliente/{cedula}/pendientes")
    Call<List<Encomienda>> obtenerPendientesPorCliente(@Path("cedula") String cedula);

    @DELETE("/api/encomiendas/{id}")
    Call<Void> eliminarEncomienda(@Path("id") int id);

    @GET("/api/encomiendas/cliente/{cedula}/historial")
    Call<List<Encomienda>> obtenerHistorial(@Path("cedula") String cedula);

    @GET("api/encomiendas/repartidor/{cedula}/historial")
    Call<List<Encomienda>> obtenerHistorialRepartidor(@Path("cedula") String cedula);


}

