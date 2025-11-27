package com.example.encomineda20.network;

import com.example.encomineda20.dto.asignador.AsignacionRequest;
import com.example.encomineda20.dto.asignador.AsignadorLoginRequest;
import com.example.encomineda20.dto.asignador.AsignadorLoginResponse;
import com.example.encomineda20.dto.asignador.AsignadorRegistroRequest;
import com.example.encomineda20.dto.asignador.AsignadorRegistroResponse;
import com.example.encomineda20.dto.asignador.AsignarEncomiendaRequest;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.dto.repartidor.RepartidorDTO;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface AsignadorApi {

    @POST("api/asignador/registro")
    Call<AsignadorRegistroResponse> registrarAsignador(
            @Body AsignadorRegistroRequest request
    );

    @POST("api/asignador/login")
    Call<AsignadorLoginResponse> loginAsignador(
            @Body AsignadorLoginRequest request
    );

    @GET("api/encomiendas/no-asignadas")
    Call<List<EncomiendaDTO>> obtenerNoAsignadas();


    @PUT("api/asignador/asignar")
    Call<Void> asignarEncomienda(@Body AsignacionRequest request);


    @PUT("api/asignador/asignar/{idEncomienda}/{cedula}")
    Call<Void> asignar(
            @Path("idEncomienda") int idEncomienda,
            @Path("cedula") String cedula
    );

    @GET("api/repartidor/todos")
    Call<List<RepartidorDTO>> obtenerRepartidores();

}

