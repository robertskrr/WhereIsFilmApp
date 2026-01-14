package com.example.whereisfilm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Busca la película
     *
     * @param titulo
     */
    private void buscar(String titulo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://streaming-availability.p.rapidapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StreamingService service = retrofit.create(StreamingService.class);

        service.searchByTitle(Config.API_KEY, Config.API_HOST, titulo)
                .enqueue(new Callback<ShowResponse>() {
                    @Override
                    public void onResponse(Call<ShowResponse> call, Response<ShowResponse> response) {
                        if (response.isSuccessful() && !response.body().result.isEmpty()) {
                            Show encontrada = response.body().result.get(0);

                            // Preparación de salto a la activity result con los datos
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            intent.putExtra("titulo", encontrada.title);
                            intent.putExtra("poster", encontrada.imageSet.verticalPoster);

                            // Sacamos las plataformas de España ("es")
                            String listaPlataformas = "";
                            if (encontrada.streamingOptions.containsKey("es")) {
                                for (StreamingOption opt : encontrada.streamingOptions.get("es")) {
                                    listaPlataformas += opt.plataforma + ","; // EN RESULT CREAR UNA LISTA A PARTIR DE ESTE STRING CON SPLIT
                                }
                            }
                            intent.putExtra("plataformas", listaPlataformas);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ShowResponse> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "ERROR AL BUSCAR", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}