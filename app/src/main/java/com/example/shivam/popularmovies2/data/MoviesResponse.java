package com.example.shivam.popularmovies2.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivam on 05/07/16.
 */
public class MoviesResponse {

    @SerializedName("results")
    @Expose
    public ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}