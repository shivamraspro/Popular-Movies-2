package com.example.shivam.popularmovies2.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivam on 11/07/16.
 */
public class TrailersResponse {

    @SerializedName("results")
    @Expose
    public ArrayList<Trailer> trailers;

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }
}
