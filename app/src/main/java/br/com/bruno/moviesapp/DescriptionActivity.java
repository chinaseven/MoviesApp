package br.com.bruno.moviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DescriptionActivity extends AppCompatActivity{

    private TextView textView;
    private ImageView imageView;
    private Bitmap poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_desc);
        textView = findViewById(R.id.descTextView);
        imageView = findViewById(R.id.movieImageView);

        DescriptionActivity.WebServiceClient client = new DescriptionActivity.WebServiceClient();
        client.execute();



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
                Gson gson = new Gson();
                Description description = gson.fromJson(json, Description.class);

                textView.setText(description.overview);
                ImageGetter imageGetter = new ImageGetter(imageView, poster);
                imageGetter.execute("https://image.tmdb.org/t/p/w500" + description.poster_path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private URL createURL (){
        try{
            String movieId = getIntent().getStringExtra("MOVIE");
            String apiKey = getString(R.string.api_key);
            String baseURL = getString(R.string.web_service_url_description);
            String urlString = baseURL;
            urlString += movieId + "?api_key=" + apiKey ;
            return new URL(urlString);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private class ImageGetter extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;
        private Bitmap bitmap;

        public ImageGetter (ImageView imageView, Bitmap bitmap){
            this.imageView = imageView;
            this.bitmap = bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... urlS) {
            try{
                URL url = new URL(urlS[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Bitmap figura = BitmapFactory.decodeStream(inputStream);
                return figura;
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap figura) {
            imageView.setImageBitmap(figura);
        }
    }

}
