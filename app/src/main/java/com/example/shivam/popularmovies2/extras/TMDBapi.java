package com.example.shivam.popularmovies2.extras;

import com.example.shivam.popularmovies2.data.MoviesResponse;
import com.example.shivam.popularmovies2.data.ReviewsResponse;
import com.example.shivam.popularmovies2.data.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shivam on 05/07/16.
 */
public interface TMDBapi {

    @GET("movie/{sort_by}")
    Call<MoviesResponse> getMovies(
            @Path("sort_by") String sort_by,
            @Query("api_key") String api_key
    );

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getTrailers(
            @Path("id") String movieId,
            @Query("api_key") String api_key
    );

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(
            @Path("id") String movieId,
            @Query("api_key") String api_key
    );
}
