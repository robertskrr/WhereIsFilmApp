package com.example.whereisfilm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText tituloBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asignarComponentes();
    }

    /**
     * Asigna los componentes de la interfaz
     */
    private void asignarComponentes() {
        tituloBuscar = findViewById(R.id.editTextTitulo);
    }

    /**
     * Se ejecuta al pulsar el botón de buscar
     *
     * @param view
     */
    public void busqueda(View view) {
        // Si no se ha escrito nada no sirve el botón
        if (tituloBuscar.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Introduce un título por favor.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Busca el título en la API
        buscar(tituloBuscar.getText().toString());

    }

    /**
     * Busca la película
     *
     * @param titulo
     */
    private void buscar(String titulo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://streaming-availability.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StreamingService service = retrofit.create(StreamingService.class);

        service.searchByTitle(Config.API_KEY, Config.API_HOST, titulo)
                .enqueue(new Callback<List<Show>>() {
                    @Override
                    public void onResponse(Call<List<Show>> call, Response<List<Show>> response) {
                        // Con un Set aseguramos que no se repitan los nombres y ordena alfabéticamente
                        Set<String> plataformasUnicas = new TreeSet<>();
                        if (response.isSuccessful() && !response.body().isEmpty() && response.body() != null) {
                            Show encontrada = response.body().get(0);

                            // Preparación de salto a la activity result con los datos
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            intent.putExtra("titulo", encontrada.title);
                            if (encontrada.imageSet != null && encontrada.imageSet.verticalPoster != null) {
                                String url = encontrada.imageSet.verticalPoster.w600;
                                intent.putExtra("poster", url);
                            }

                            // Sacamos las plataformas de España ("es")

                            if (encontrada.streamingOptions != null && encontrada.streamingOptions.containsKey("es")) {
                                for (StreamingOption opt : encontrada.streamingOptions.get("es")) {
                                    if (opt.plataforma != null && opt.plataforma.name != null) {
                                        plataformasUnicas.add(opt.plataforma.name);
                                    }
                                }
                            }

                            // Convertir el Set a String nuevamente
                            StringBuilder listaPlataformas = new StringBuilder();
                            for (String nombre : plataformasUnicas) {
                                if (listaPlataformas.length() > 0) {
                                    listaPlataformas.append(" -- "); // Separa los elementos
                                }
                                listaPlataformas.append(nombre);
                            }
                            intent.putExtra("plataformas", listaPlataformas.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Show>> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "ERROR AL BUSCAR", Toast.LENGTH_SHORT).show();
                        // Esto imprimirá el error real en la pestaña "Logcat" de Android Studio
                        System.err.println(throwable.getMessage());
                        android.util.Log.e("API_ERROR", "Causa: " + throwable.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}