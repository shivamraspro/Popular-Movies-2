package com.example.shivam.popularmovies2.ui;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.shivam.popularmovies2.BuildConfig;
import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.adapters.MovieAdapter;
import com.example.shivam.popularmovies2.data.Movie;
import com.example.shivam.popularmovies2.data.MoviesResponse;
import com.example.shivam.popularmovies2.database.FvMovieColumns;
import com.example.shivam.popularmovies2.database.FvMovieProvider;
import com.example.shivam.popularmovies2.extras.TMDBapi;
import com.example.shivam.popularmovies2.extras.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shivam on 05/07/16.
 */
public class MovielistFragment extends Fragment {//implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovielistFragment.class.getSimpleName();
    private ArrayList<Movie> movies;
    private boolean mTwoPane;
    private boolean loadingForFirstTime;
    private boolean restoring;
    private boolean sort_by_favourites;

    private ViewGroup fragmentContainer;

    private int mPosition;// = GridView.INVALID_POSITION;

    private static final String MOVIES_KEY = "movies";
    private static final String SELECTED_KEY = "selected_position";
    private static final String RESTORING_KEY = "restore?";
    private static final String TWO_PANE_KEY = "two_pane";

    private String sort_by;

    private SwipeRefreshLayout swipeRefreshLayout;

    MovieAdapter movieAdapter;
    GridView gridView;

    private final String[] MOVIE_COLUMNS = {
            FvMovieColumns.movieId,
            FvMovieColumns.posterPath,
            FvMovieColumns.plotSynopsis,
            FvMovieColumns.releaseDate,
            FvMovieColumns.originalTitle,
            FvMovieColumns.userRating
    };

    final int COL_MOVIE_ID = 0;
    final int COL_POSTER_PATH = 1;
    final int COL_PLOT_SYNOPSIS = 2;
    final int COL_RELEASE_DATE = 3;
    final int COL_ORIGINAL_TITLE = 4;
    final int COL_USER_RATING = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            restoring = savedInstanceState.getBoolean(RESTORING_KEY);
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            mTwoPane = savedInstanceState.getBoolean(TWO_PANE_KEY);
        } else {
            restoring = false;
            loadingForFirstTime = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentContainer = container;

        View rootView = inflater.inflate(R.layout.fragment_movielist, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview_posters);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingForFirstTime = true;
                if (sort_by_favourites)
                    loadMoviesFromDatabase();
                else
                    loadMoviesFromNetwork();
            }
        });

        sort_by = Utility.getCurrentSortByValue(getActivity());
        sort_by_favourites = Utility.isSortByFavs(sort_by);

        if (restoring && savedInstanceState != null) {
            restoring = false;
            showMovies();
        } else {
            if (sort_by_favourites)
                loadMoviesFromDatabase();
            else if (Utility.isNetworkAvailable(getActivity()))
                loadMoviesFromNetwork();
        }

        return rootView;
    }

    private void loadMoviesFromNetwork() {

        sort_by = Utility.getCurrentSortByValue(getActivity());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utility.MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBapi TMDBapi = retrofit.create(TMDBapi.class);

        Call<MoviesResponse> call = TMDBapi.getMovies(sort_by, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new retrofit2.Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                try {
                    movies = response.body().getMovies();

                    if (loadingForFirstTime)
                        swipeRefreshLayout.setRefreshing(false);

                    showMovies();

                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "Response Code : " + response.code() + " //" + response.message());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    private void loadMoviesFromDatabase() {

        Cursor movieCursor = null;
        try {
            movieCursor = getActivity().getContentResolver().query(
                    FvMovieProvider.Moviedb.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed Querying DB " + e.getMessage());
        }

        ArrayList<Movie> tempMovies = new ArrayList<Movie>();
        Movie tempMovie;
        if (movieCursor.moveToFirst()) {
            do {
                tempMovie = new Movie();

                tempMovie.setMovieId(movieCursor.getString(COL_MOVIE_ID));
                tempMovie.setPosterPath(movieCursor.getString(COL_POSTER_PATH));
                tempMovie.setPlotSynopsis(movieCursor.getString(COL_PLOT_SYNOPSIS));
                tempMovie.setReleaseDate(movieCursor.getString(COL_RELEASE_DATE));
                tempMovie.setOriginalTitle(movieCursor.getString(COL_ORIGINAL_TITLE));
                tempMovie.setUserRating(movieCursor.getString(COL_USER_RATING));

                tempMovies.add(tempMovie);

            } while (movieCursor.moveToNext());
        }

        movies = tempMovies;

        if (loadingForFirstTime)
            swipeRefreshLayout.setRefreshing(false);

        showMovies();

    }

    private void showMovies() {

        if (movies != null && movies.size() > 0) {
            movieAdapter = new MovieAdapter(getActivity(), movies);

            if (mTwoPane) {
                gridView.setNumColumns(3);
                gridView.setAdapter(movieAdapter);

                if (loadingForFirstTime && !restoring) {
                    loadingForFirstTime = false;
                    gridView.setItemChecked(0, true);
                    ((Callback) getActivity()).onMovielistFragmentCreated(movies.get(0));
                } else {
                    gridView.setItemChecked(mPosition, true);
                    ((Callback) getActivity()).onMovielistFragmentCreated(movies.get(mPosition));
                }
            } else {
                gridView.setNumColumns(2);
                gridView.setAdapter(movieAdapter);
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPosition = position;
                    ((Callback) getActivity()).onItemSelected(movies.get(position));
                }
            });
        } else {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.fragment_movielist, fragmentContainer, false);

            if (mTwoPane)
                ((Callback) getActivity()).onMovielistFragmentCreated(null);
        }
    }

    public void onSortByValueChanged(String sortByChoice) {
        sort_by = sortByChoice;
        sort_by_favourites = Utility.isSortByFavs(sort_by);
        loadingForFirstTime = true;

        if (sort_by_favourites)
            loadMoviesFromDatabase();
        else
            loadMoviesFromNetwork();
    }

    public interface Callback {
        /**
         * MovieListFragment Callback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);

        public void onMovielistFragmentCreated(Movie movie);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to GridView.INVALID_POSITION,
        // so check for that before storing.
        outState.putBoolean(TWO_PANE_KEY, mTwoPane);

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        outState.putParcelableArrayList(MOVIES_KEY, movies);

        outState.putBoolean(RESTORING_KEY, true);

        super.onSaveInstanceState(outState);
    }

    public void setTwoPane(boolean b) {
        mTwoPane = b;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movielistfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            loadingForFirstTime = true;
            swipeRefreshLayout.setRefreshing(true);
            if (sort_by_favourites)
                loadMoviesFromDatabase();
            else
                loadMoviesFromNetwork();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
