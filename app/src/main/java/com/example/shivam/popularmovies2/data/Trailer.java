package com.example.shivam.popularmovies2.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 11/07/16.
 */
public class Trailer implements Parcelable {

    @SerializedName("id")
    @Expose
    private String trailerId;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String trailerName;

    @SerializedName("type")
    @Expose
    private String trailerType;

    private Trailer(Parcel source) {
        trailerId = source.readString();
        key = source.readString();
        trailerName = source.readString();
        trailerType = source.readString();
    }

    public Trailer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerId);
        dest.writeString(key);
        dest.writeString(trailerName);
        dest.writeString(trailerType);
    }

    //It will unwrap the parcel and create an instance of type MovieInfo
    //and named as CREATOR
    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {

        //Creates new instance of Parcelable class
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[0];
        }
    };

    public String getTrailerId() {
        return trailerId;
    }

    public String getKey() {
        return key;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public void setKey(String key) {
        this.key= key;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

}
