package com.example.encomineda20.network;



import com.example.encomineda20.dto.cliente.ClienteDTO;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.dto.encomienda.EncomiendaResponse;
import com.example.encomineda20.dto.cliente.LoginRequest;
import com.example.encomineda20.dto.cliente.LoginResponse;
import com.example.encomineda20.dto.cliente.PagoRequest;
import com.example.encomineda20.dto.repartidor.CalificacionRequest;
import com.example.encomineda20.dto.repartidor.MensajeResponse;
import com.example.encomineda20.modelo.Encomienda;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

    public interface ApiService {
        @POST("api/clientes/registro")
        Call<Map<String, Object>> registrarCliente(@Body ClienteDTO cliente);

        @POST("api/clientes/login")
        Call<LoginResponse> loginCliente(@Body LoginRequest request);

        @GET("api/encomiendas/cliente/{cedula}")
        Call<List<Encomienda>> obtenerPorCliente(@Path("cedula") String cedula);

        @POST("api/encomiendas/pagar/{id}")
        Call<Encomienda> pagarEncomienda(
                @Path("id") int idEncomienda,
                @Query("metodoPago") String metodoPago
        );

        @PUT("api/encomiendas/{id}/pago")
        Call<EncomiendaResponse> pagarEncomienda(
                @Path("id") int idEncomienda,
                @Body PagoRequest pagoRequest
        );

        @GET("api/clientes/{cedula}/notificaciones")
        Call<List<EncomiendaDTO>> obtenerNotificaciones(@Path("cedula") String cedula);


        @PUT("api/encomiendas/{id}/calificar")
        Call<MensajeResponse> calificarEncomienda(
                @Path("id") int idEncomienda,
                @Body CalificacionRequest request
        );




    }
