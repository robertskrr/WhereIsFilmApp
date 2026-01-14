package com.example.whereisfilm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * @author Robert
 * Servicio que implementa la API y devuelve el título y el tipo
 */
public interface StreamingService {
    @GET("shows/search/title?country=es")
    Call<List<Show>> searchByTitle(
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String host,
            @Query("title") String movieTitle
    );
}
