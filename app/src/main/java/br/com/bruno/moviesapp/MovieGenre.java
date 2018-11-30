package br.com.bruno.moviesapp;

public class MovieGenre {

    public final int id;
    public final String genre;

    public MovieGenre(int id, String genre){
        this.id = id;
        this.genre = genre;
    }
    public MovieGenre(Genre genresData){
        this.id = genresData.id;
        this.genre = genresData.name;
    }
}
