package com.example.shivam.popularmovies2.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.shivam.popularmovies2.R;

/**
 * Created by shivam on 07/07/16.
 */
public class Utility {

    public static final String SELECTED_MOVIE = "selected_movie";
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentSortByValue(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.sort_by_key),
                context.getString(R.string.sort_by_default));
    }

    public static int getMainActivityTitle(String sortByChoice) {

        switch (sortByChoice) {
            case "popular" : return R.string.sort_by_label_popular;
            case "top_rated" : return R.string.sort_by_label_top_rated;
            case "user_fav" : return R.string.sort_by_label_favourites;
        }
        return 0;
    }

    public static boolean isSortByFavs(String sort_by) {
        if(sort_by.equals("user_fav"))
            return true;
        else
            return false;
    }
}
