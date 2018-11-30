package br.com.bruno.moviesapp;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private ListView movieListView;
    private MovieArrayAdapter adapter;
    private List<MovieMovie> movieList;
    int count_pages=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        movieListView = findViewById(R.id.movieListView);
        movieList = new ArrayList<>();
        adapter = new MovieArrayAdapter(this, movieList);
        movieListView.setAdapter(adapter);

        count_pages = 1;
        MovieActivity.WebServiceClient client = new MovieActivity.WebServiceClient();
        //MovieActivity.WebServiceClient client2 = new MovieActivity.WebServiceClient();
        //MovieActivity.WebServiceClient client3 = new MovieActivity.WebServiceClient();
        client.execute("1");
        //client2.execute("2");
        //client3.execute("3");

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieMovie movie = (MovieMovie) parent.getAdapter().getItem(position);
                String s = String.valueOf(movie.id) ;
                Toast toast = Toast.makeText(parent.getContext(), "teste "+s, Toast.LENGTH_SHORT);
                toast.show();

                Intent intent =
                        new Intent(MovieActivity.this,
                                DescriptionActivity.class);
                intent.putExtra("MOVIE", s);
                startActivity(intent);
            }
        });

    }

    private class WebServiceClient extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = createURL(Integer.parseInt(params[0]));
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

            try {
                Gson gson = new Gson();
                MovieData filmes = gson.fromJson(json, MovieData.class);
                if (filmes.total_pages>=count_pages && count_pages<=3){
                    count_pages++;
                    if (count_pages == 3){
                        for (int i = 0 ;i<10;i++) {
                            Movie data =filmes.getList().get(i);
                            movieList.add(new MovieMovie(data));
                        }
                    }else{
                        for (Movie data : filmes.getList()) {
                            movieList.add(new MovieMovie(data));
                        }
                    }

                }else{
                    return;
                }

                adapter.notifyDataSetChanged();

                MovieActivity.WebServiceClient client = new MovieActivity.WebServiceClient();
                client.execute(String.valueOf( count_pages));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private URL createURL (int page){
        try{

            String genero = getIntent().getStringExtra("GENRE");
            String apiKey = getString(R.string.api_key);
            String baseURL = getString(R.string.web_service_url_movies);
            String urlString = baseURL;
            urlString += "api_key=" + apiKey + "&with_genres="+ genero+"&page="+page;
            //urlString += "api_key=" + apiKey + "&with_genres="+ "14"+"&page="+page;
            return new URL(urlString);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
