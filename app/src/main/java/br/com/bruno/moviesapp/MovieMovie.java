package br.com.bruno.moviesapp;

public class MovieMovie {
        public final int id;
        public final String title;

        public MovieMovie(int id , String movie){
            this.title = movie;
            this.id=id;
        }

        public MovieMovie(Movie movie){
            this.title = movie.title;
            this.id=movie.id;
        }

}
