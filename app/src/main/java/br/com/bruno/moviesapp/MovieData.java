package br.com.bruno.moviesapp;

import java.util.List;

public class MovieData {

        //int total_results;
        //int total_pages;
    int page;
    int total_pages;
    private List<Movie> results;

    public List<Movie> getList() {
        return results;
    }

    public void setList(List<Movie> movies) {
        this.results = movies;
    }
}
