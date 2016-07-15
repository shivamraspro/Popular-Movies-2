package com.example.shivam.popularmovies2.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 11/07/16.
 */
public class Review implements Parcelable {

    @SerializedName("id")
    @Expose
    private String reviewId;

    @SerializedName("author")
    @Expose
    private String authorName;

    @SerializedName("content")
    @Expose
    private String reviewContent;

    private Review(Parcel source) {
        reviewId = source.readString();
        authorName = source.readString();
        reviewContent = source.readString();
    }

    public Review() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(authorName);
        dest.writeString(reviewContent);
    }

    //It will unwrap the parcel and create an instance of type MovieInfo
    //and named as CREATOR
    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {

        //Creates new instance of Parcelable class
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[0];
        }
    };

    public String getReviewId() {
        return reviewId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
