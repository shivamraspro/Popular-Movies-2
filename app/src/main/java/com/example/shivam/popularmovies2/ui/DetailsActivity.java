package com.example.shivam.popularmovies2.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.extras.Utility;
import com.example.shivam.popularmovies2.data.Movie;

/**
 * Created by shivam on 10/07/16.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Movie selectedMovie = getIntent().getParcelableExtra(Utility.SELECTED_MOVIE);
            arguments.putParcelable(Utility.SELECTED_MOVIE, selectedMovie);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

}
