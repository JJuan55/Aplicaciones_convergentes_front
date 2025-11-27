package com.example.encomineda20.network;

import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.dto.repartidor.EntregaEncomiendaRequest;
import com.example.encomineda20.dto.repartidor.EstadisticasDTO;
import com.example.encomineda20.dto.repartidor.MensajeResponse;
import com.example.encomineda20.dto.repartidor.RegistroRepartidorRequest;
import com.example.encomineda20.dto.repartidor.RepartidorDTO;
import com.example.encomineda20.dto.repartidor.RepartidorLoginRequest;
import com.example.encomineda20.dto.repartidor.RepartidorLoginResponse;
import com.example.encomineda20.dto.repartidor.ResenaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RepartidorApi {

    @POST("api/repartidor/login")
    Call<RepartidorLoginResponse> login(@Body RepartidorLoginRequest request);

    @POST("api/repartidor/registrar")
    Call<RepartidorLoginResponse> registrar(@Body RegistroRepartidorRequest request);

    @GET("api/repartidor/encomiendas/asignadas")
    Call<List<EncomiendaDTO>> obtenerEncomiendasAsignadas(@Query("cedulaRepartidor") String cedulaRepartidor);


    @PUT("api/repartidor/encomienda/entregar")
    Call<MensajeResponse> entregarEncomienda(@Body EntregaEncomiendaRequest request);

    @GET("api/repartidor/estadisticas")
    Call<EstadisticasDTO> obtenerEstadisticas(@Query("cedulaRepartidor") String cedula);


    @GET("api/repartidor/{cedula}/resenas")
    Call<List<ResenaDTO>> obtenerResenas(@Path("cedula") String cedula);

    @GET("/api/repartidor/{cedula}")
    Call<RepartidorDTO> obtenerPorCedula(@Path("cedula") String cedula);

}
