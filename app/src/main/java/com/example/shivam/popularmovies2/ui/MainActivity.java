package com.example.shivam.popularmovies2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.extras.Utility;
import com.example.shivam.popularmovies2.data.Movie;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity implements MovielistFragment.Callback {

    private boolean mTwoPane;
    private String mSortByChoice;
    private String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSortByChoice = Utility.getCurrentSortByValue(this);

        getSupportActionBar().setTitle(Utility.getMainActivityTitle(mSortByChoice));

        setContentView(R.layout.activity_main);

        setupStetho();

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            MovielistFragment mF = (MovielistFragment) getFragmentManager().findFragmentById(R.id.movielist_fragment);
            if (mF != null)
                mF.setTwoPane(true);

        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortByChoice = Utility.getCurrentSortByValue(this);

        if (sortByChoice != null) {
            getSupportActionBar().setTitle(Utility.getMainActivityTitle(sortByChoice));
            MovielistFragment mF = (MovielistFragment) getFragmentManager().findFragmentById(R.id.movielist_fragment);
            if (mF != null)
                mF.onSortByValueChanged(sortByChoice);
        }

        mSortByChoice = sortByChoice;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        );
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putParcelable(Utility.SELECTED_MOVIE, movie);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Utility.SELECTED_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onMovielistFragmentCreated(Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(Utility.SELECTED_MOVIE, movie);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);

        getFragmentManager().beginTransaction().replace(R.id.movie_detail_container,
                fragment, DETAILFRAGMENT_TAG).commit();

    }
}
