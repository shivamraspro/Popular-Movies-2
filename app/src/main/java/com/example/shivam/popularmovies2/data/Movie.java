package com.example.shivam.popularmovies2.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 05/07/16.
 */
public class Movie implements Parcelable {
    @SerializedName("id")
    @Expose
    private String movieId;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("overview")
    @Expose
    private String plotSynopsis;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("vote_average")
    @Expose
    private String userRating;


    //This constructor will be used to create an instance of the class from the parcel
    private Movie(Parcel source) {
        movieId = source.readString();
        posterPath = source.readString();
        plotSynopsis = source.readString();
        releaseDate = source.readString();
        originalTitle = source.readString();
        userRating = source.readString();
    }

    //The above constructor hides the default constructor
    public Movie() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(posterPath);
        dest.writeString(plotSynopsis);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeString(userRating);
    }

    //It will unwrap the parcel and create an instance of type MovieInfo
    //and named as CREATOR
    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        //Creates new instance of Parcelable class
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    //SETTERS
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }



    //GETTERS
    public String getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getPosterURL() {
        return "http://image.tmdb.org/t/p/w342" + posterPath;
    }


}
