package com.example.encomineda20.dto;

import com.example.encomineda20.network.EncomiendaApiService;
import com.example.encomineda20.network.RepartidorApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8081/";

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static EncomiendaApiService getEncomiendaService() {
        return getInstance().create(EncomiendaApiService.class);
    }
}
