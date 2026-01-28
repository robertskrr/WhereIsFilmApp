package com.example.whereisfilm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText tituloBuscar;
    private SharedPreferences prefs;

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
        prefs = getSharedPreferences("MisPrefs", MODE_PRIVATE);
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

    public void historialBusqueda(View view) {
        startActivity(new Intent(MainActivity.this, Historial.class));
    }

    /**
     * Guarda la búsqueda en SharedPreferences
     */
    public void guardarBusqueda(String titulo) {
        // Fecha y hora actual
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // Mostrar el titulo junto con la fecha
        String nuevaBusqueda = titulo + " - " + fecha;

        // Si hay un historial anterior recogerlo para unirlo
        String historialPrevio = prefs.getString("historial", "");

        // Unirlo con lo anterior usando un salto de línea
        String historialActualizado;
        if (historialPrevio.isEmpty()) {
            // Si esta vacío lo inicia simplemente con lo nuevo
            historialActualizado = nuevaBusqueda;
        } else {
            // Separamos las busquedas con \n
            historialActualizado = nuevaBusqueda + "\n" + historialPrevio;
        }

        // Lo guardamos
        prefs.edit().putString("historial", historialActualizado).apply();
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

        service.searchByTitle(Config.API_KEY, Config.API_HOST, titulo, "es")
                .enqueue(new Callback<List<Show>>() {
                    @Override
                    public void onResponse(Call<List<Show>> call, Response<List<Show>> response) {
                        // Con un Set aseguramos que no se repitan los nombres y ordena alfabéticamente
                        if (response.isSuccessful() && !response.body().isEmpty() && response.body() != null) {
                            List<Show> resultados = response.body();
                            Show encontrada = null;

                            // Búsqueda exacta de lo que se ha escrito
                            for (Show s : resultados) {
                                if (s.title.equalsIgnoreCase(titulo.trim())) {
                                    encontrada = s;
                                    break;
                                }
                            }

                            // Si después de buscar no encuentra coincidencia exacta se queda con la primera relevante
                            if (encontrada == null) {
                                encontrada = resultados.get(0);
                            }

                            // Guardamos la búsqueda en el historial
                            guardarBusqueda(encontrada.title);
                            // Preparación de salto a la activity result con los datos
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            intent.putExtra("titulo", encontrada.title);

                            if (encontrada.imageSet != null && encontrada.imageSet.verticalPoster != null) {
                                String url = encontrada.imageSet.verticalPoster.w600;
                                intent.putExtra("poster", url);
                            }

                            // HashMap para evitar duplicados por nombre, guardando el objeto StreamingOption
                            HashMap<String, StreamingOption> filtro = new HashMap<>();
                            // Sacamos las plataformas de España ("es")
                            if (encontrada.streamingOptions != null && encontrada.streamingOptions.containsKey("es")) {
                                for (StreamingOption opt : encontrada.streamingOptions.get("es")) {
                                    if (opt.plataforma != null && opt.plataforma.name != null) {
                                        // Si el nombre de la plataforma no está en el filtro, lo añadimos con su objeto
                                        if (!filtro.containsKey(opt.plataforma)) {
                                            filtro.put(opt.plataforma.name, opt);
                                        }
                                    }
                                }
                            }

                            // Convertimos el contenido del filtro en una lista para enviar
                            ArrayList<StreamingOption> listaPlataformas = new ArrayList<>(filtro.values());

                            // Enviamos la lista de objetos (nombre + link)
                            intent.putExtra("listaPlataformas", listaPlataformas);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Show>> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "ERROR AL BUSCAR", Toast.LENGTH_SHORT).show();
                        android.util.Log.e("API_ERROR", "Causa: " + throwable.getMessage());
                    }
                });
    }
}