package br.com.bruno.moviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {

    private ListView genreListView;
    private MovieGenresArrayAdapter adapter;
    private List<MovieGenre> movieGenreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        genreListView = findViewById(R.id.genreListView);
        movieGenreList = new ArrayList<>();
        adapter = new MovieGenresArrayAdapter(this, movieGenreList);
        genreListView.setAdapter(adapter);

        WebServiceClient client = new WebServiceClient();
        client.execute();
        genreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieGenre movieGenre = (MovieGenre) parent.getAdapter().getItem(position);
                String s = String.valueOf(movieGenre.id) ;
                Toast toast = Toast.makeText(parent.getContext(), "teste "+s, Toast.LENGTH_SHORT);
                toast.show();

                Intent intent =
                        new Intent(MainActivity.this,
                                MovieActivity.class);
                intent.putExtra("GENRE", s);
                startActivity(intent);
            }
        });
    }

    private class WebServiceClient extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = createURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String linha = null;
                StringBuilder stringBuilder = new StringBuilder ("");
                while ((linha = reader.readLine()) != null){
                    stringBuilder.append(linha);
                }
                return stringBuilder.toString();
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String json) {
            //Toast.makeText(MainActivity.this, json, Toast.LENGTH_SHORT).show();
            try {
                movieGenreList.clear();
                Gson gson = new Gson();
                Genres generos = gson.fromJson(json, Genres.class);
                for (Genre data : generos.getList()) {
                    movieGenreList.add(new MovieGenre(data));
                }
                /*JSONObject previsoes = new JSONObject(json);
                JSONArray list = previsoes.getJSONArray("list");
                for (int i = 0; i < list.length(); i++){
                    JSONObject previsao = list.getJSONObject(i);
                    long dt = previsao.getLong("dt");
                    JSONObject main = previsao.getJSONObject("main");
                    double temp_min = main.getDouble("temp_min");
                    double temp_max = main.getDouble("temp_max");
                    int humidity = main.getInt ("humidity");
                    String description = previsao.getJSONArray("weather").getJSONObject(0).
                            getString("description");
                    String icon =  previsao.getJSONArray("weather").getJSONObject(0).
                            getString("icon");
                    Weather weather = new Weather(dt,
                            temp_min, temp_max, humidity,
                            description, icon);
                    weatherList.add(weather);
                }*/
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private URL createURL (){
        try{
            String apiKey = getString(R.string.api_key);
            String baseURL = getString(R.string.web_service_url);
            String urlString = baseURL;
            urlString += "api_key=" + apiKey + "&language=pt-BR";
            return new URL(urlString);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
