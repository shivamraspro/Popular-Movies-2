package com.example.shivam.popularmovies2.ui;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivam.popularmovies2.BuildConfig;
import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.adapters.ReviewAdapter;
import com.example.shivam.popularmovies2.adapters.TrailerAdapter;
import com.example.shivam.popularmovies2.data.Movie;
import com.example.shivam.popularmovies2.data.Review;
import com.example.shivam.popularmovies2.data.ReviewsResponse;
import com.example.shivam.popularmovies2.data.Trailer;
import com.example.shivam.popularmovies2.data.TrailersResponse;
import com.example.shivam.popularmovies2.database.FvMovieColumns;
import com.example.shivam.popularmovies2.database.FvMovieProvider;
import com.example.shivam.popularmovies2.database.FvMovieReviewsColumns;
import com.example.shivam.popularmovies2.database.FvMovieTrailersColumns;
import com.example.shivam.popularmovies2.extras.TMDBapi;
import com.example.shivam.popularmovies2.extras.Utility;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shivam on 10/07/16.
 */
public class DetailsFragment extends Fragment {

    private String LOG_TAG = DetailsFragment.class.getSimpleName();
    private boolean isFavourite = false;
    private boolean sort_by_favourites;

    private Movie selectedMovie;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    private final String MOVIE_KEY = "movie";
    private final String TRAILERS_KEY = "trailers";
    private final String REVIEWS_KEY = "reviews";

    private ImageView mImageView;
    private TextView mTitle;
    private TextView mUserRating;
    private TextView mSynopsis;
    private TextView mReleaseDate;

    private ImageView mFavourites_star;

    private ExpandableHeightListView mTrailersListView;
    private ExpandableHeightListView mReviewsListView;

    private Context mContext;

    private Retrofit retrofit;
    private TMDBapi TMDBapi;

    private final String[] TRAILER_COLUMNS = {
            FvMovieTrailersColumns.trailerId,
            FvMovieTrailersColumns.key,
            FvMovieTrailersColumns.trailerName,
            FvMovieTrailersColumns.trailerType,
    };

    final int COL_TRAILER_ID = 0;
    final int COL_TRAILER_KEY = 1;
    final int COL_TRAILER_NAME = 2;
    final int COL_TRAILER_TYPE = 3;

    private final String[] REVIEW_COLUMNS = {
            FvMovieReviewsColumns.reviewId,
            FvMovieReviewsColumns.authorName,
            FvMovieReviewsColumns.reviewContent,
    };

    final int COL_REVIEW_ID = 0;
    final int COL_REVIEW_NAME = 1;
    final int COL_REVIEW_CONTENT = 2;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sort_by_favourites = Utility.isSortByFavs(Utility.getCurrentSortByValue(getActivity()));

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                selectedMovie = arguments.getParcelable(Utility.SELECTED_MOVIE);
            }
        } else {
            selectedMovie = savedInstanceState.getParcelable(MOVIE_KEY);
            trailers = savedInstanceState.getParcelableArrayList(TRAILERS_KEY);
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(Utility.MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBapi = retrofit.create(TMDBapi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        if(selectedMovie != null) {
            if (savedInstanceState == null) {
                if (sort_by_favourites) {
                    loadTrailersFromDatabase(rootView);
                    loadReviewsFromDatabase(rootView);
                } else {
                    loadTrailersFromNetwork(selectedMovie.getMovieId(), rootView);
                    loadReviewsFromNetwork(selectedMovie.getMovieId(), rootView);
                }
            } else {
                if (trailers != null && trailers.size() > 0)
                    showTrailers(rootView);

                if (reviews != null && reviews.size() > 0)
                    showReviews(rootView);
            }

            mImageView = (ImageView) rootView.findViewById(R.id.thumbnail);
            mUserRating = (TextView) rootView.findViewById(R.id.userRating);
            mReleaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
            mSynopsis = (TextView) rootView.findViewById(R.id.synopsis);
            mTitle = (TextView) rootView.findViewById(R.id.title);
            mFavourites_star = (ImageView) rootView.findViewById(R.id.favourites_star);

            isFavourite = checkFavourite();

            try {
                try {
                    Picasso.with(mContext)
                            .load(selectedMovie.getPosterURL())
                            .error(R.drawable.ic_error)
                            .into(mImageView);

                } catch (IllegalArgumentException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

                mTitle.setText(selectedMovie.getOriginalTitle());
                mUserRating.setText(selectedMovie.getUserRating() + "/10");
                mReleaseDate.setText(selectedMovie.getReleaseDate());
                mSynopsis.setText(selectedMovie.getPlotSynopsis());

                if (isFavourite)
                    mFavourites_star.setImageResource(android.R.drawable.btn_star_big_on);
                else
                    mFavourites_star.setImageResource(android.R.drawable.btn_star_big_off);

            } catch (NullPointerException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            mFavourites_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavourite)
                        turnOffStar();
                    else
                        turnOnStar();
                }
            });
        }
        return rootView;
    }

    private boolean checkFavourite() {
        Cursor movieCursor = null;

        try {
            movieCursor = mContext.getContentResolver().query(
                    FvMovieProvider.Moviedb.withId(selectedMovie.getMovieId()),
                    null,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed Querying DB " + e.getMessage());
        }

        if (movieCursor != null && movieCursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    private void turnOffStar() {
        mFavourites_star.setImageResource(android.R.drawable.btn_star_big_off);
        isFavourite = false;
        Toast.makeText(mContext, "Removed from Favourites!", Toast.LENGTH_SHORT).show();

        deleteMovieFromDB();
    }

    private void turnOnStar() {
        mFavourites_star.setImageResource(android.R.drawable.btn_star_big_on);
        isFavourite = true;
        Toast.makeText(mContext, "Added to Favourites!", Toast.LENGTH_SHORT).show();

        addMovietoDB();
    }

    private void addMovietoDB() {

        String mId = selectedMovie.getMovieId();

        try {

            ContentValues movieValues = new ContentValues();
            movieValues.put(FvMovieColumns.movieId, mId);
            movieValues.put(FvMovieColumns.posterPath, selectedMovie.getPosterPath());
            movieValues.put(FvMovieColumns.plotSynopsis, selectedMovie.getPlotSynopsis());
            movieValues.put(FvMovieColumns.releaseDate, selectedMovie.getReleaseDate());
            movieValues.put(FvMovieColumns.originalTitle, selectedMovie.getOriginalTitle());
            movieValues.put(FvMovieColumns.userRating, selectedMovie.getUserRating());

            mContext.getContentResolver().insert(FvMovieProvider.Moviedb.CONTENT_URI, movieValues);

            if (trailers.size() > 0) {
                Vector<ContentValues> tVector = new Vector<>(trailers.size());
                for (Trailer trailer : trailers) {
                    ContentValues cv = new ContentValues();
                    cv.put(FvMovieTrailersColumns.trailerId, trailer.getTrailerId());
                    cv.put(FvMovieTrailersColumns.key, trailer.getKey());
                    cv.put(FvMovieTrailersColumns.trailerName, trailer.getTrailerName());
                    cv.put(FvMovieTrailersColumns.trailerType, trailer.getTrailerType());
                    cv.put(FvMovieTrailersColumns.movieId, mId);

                    tVector.add(cv);
                }

                ContentValues[] trailerValues = new ContentValues[trailers.size()];
                tVector.toArray(trailerValues);
                mContext.getContentResolver().bulkInsert(
                        FvMovieProvider.Trailerdb.CONTENT_URI,
                        trailerValues);
            }

            if (reviews.size() > 0) {
                Vector<ContentValues> rVector = new Vector<>(reviews.size());
                for (Review review : reviews) {
                    ContentValues cv = new ContentValues();
                    cv.put(FvMovieReviewsColumns.reviewId, review.getReviewId());
                    cv.put(FvMovieReviewsColumns.authorName, review.getAuthorName());
                    cv.put(FvMovieReviewsColumns.reviewContent, review.getReviewContent());
                    cv.put(FvMovieReviewsColumns.movieId, mId);

                    rVector.add(cv);
                }

                ContentValues[] reviewValues = new ContentValues[reviews.size()];
                rVector.toArray(reviewValues);
                mContext.getContentResolver().bulkInsert(
                        FvMovieProvider.Reviewdb.CONTENT_URI,
                        reviewValues);
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed Inserting Movie : " + e.getMessage());
        }

    }

    private void deleteMovieFromDB() {

        String mId = selectedMovie.getMovieId();

        try {
            mContext.getContentResolver().delete(
                    FvMovieProvider.Moviedb.withId(mId),
                    null,
                    null);

            if (trailers.size() > 0) {
                mContext.getContentResolver().delete(
                        FvMovieProvider.Trailerdb.withMovieId(mId),
                        null,
                        null
                );
            }

            if (reviews.size() > 0) {
                mContext.getContentResolver().delete(
                        FvMovieProvider.Reviewdb.withMovieId(mId),
                        null,
                        null
                );
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed Deleting Movie : " + e.getMessage());
        }

    }

    private void loadTrailersFromNetwork(String movieId, final View rootView) {
        Call<TrailersResponse> call1 = TMDBapi.getTrailers(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        call1.enqueue(new retrofit2.Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                try {
                    trailers = response.body().getTrailers();

                    if(trailers != null && trailers.size() > 0)
                        showTrailers(rootView);

                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "Response Code : " + response.code() + " //" + response.message());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    private void loadReviewsFromNetwork(String movieId, final View rootView) {

        Call<ReviewsResponse> call2 = TMDBapi.getReviews(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        call2.enqueue(new retrofit2.Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                try {
                    reviews = response.body().getReviews();

                    if(reviews != null && reviews.size() > 0)
                        showReviews(rootView);

                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "Response Code : " + response.code() + " //" + response.message());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    private void loadTrailersFromDatabase(final View rootView) {
        Cursor trailerCursor = null;
        try {
            trailerCursor = mContext.getContentResolver().query(
                    FvMovieProvider.Trailerdb.withMovieId(selectedMovie.getMovieId()),
                    TRAILER_COLUMNS,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed Querying DB " + e.getMessage());
        }

        ArrayList<Trailer> tempTrailers = new ArrayList<Trailer>();
        Trailer tempTrailer;
        if (trailerCursor.moveToFirst()) {
            do {
                tempTrailer = new Trailer();
                tempTrailer.setTrailerId(trailerCursor.getString(COL_TRAILER_ID));
                tempTrailer.setKey(trailerCursor.getString(COL_TRAILER_KEY));
                tempTrailer.setTrailerName(trailerCursor.getString(COL_TRAILER_NAME));
                tempTrailer.setTrailerType(trailerCursor.getString(COL_TRAILER_TYPE));

                tempTrailers.add(tempTrailer);

            } while (trailerCursor.moveToNext());
        }

        trailers = tempTrailers;

        if(trailers != null && trailers.size() > 0)
            showTrailers(rootView);
    }

    private void loadReviewsFromDatabase(final View rootView) {
        Cursor reviewCursor = null;
        try {
            reviewCursor = mContext.getContentResolver().query(
                    FvMovieProvider.Reviewdb.withMovieId(selectedMovie.getMovieId()),
                    REVIEW_COLUMNS,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed Querying DB " + e.getMessage());
        }

        ArrayList<Review> tempReviews = new ArrayList<Review>();
        Review tempReview;

        if (reviewCursor.moveToFirst()) {
            do {
                tempReview = new Review();
                tempReview.setReviewId(reviewCursor.getString(COL_REVIEW_ID));
                tempReview.setAuthorName(reviewCursor.getString(COL_REVIEW_NAME));
                tempReview.setReviewContent(reviewCursor.getString(COL_REVIEW_CONTENT));

                tempReviews.add(tempReview);

            } while (reviewCursor.moveToNext());
        }

        reviews = tempReviews;

        if(reviews != null && reviews.size() > 0)
            showReviews(rootView);
    }

    private void showTrailers(final View rootView) {

        TrailerAdapter trailerAdapter = new TrailerAdapter(mContext, trailers);
        mTrailersListView = (ExpandableHeightListView) rootView.findViewById(R.id.listview_trailers);
        mTrailersListView.setExpanded(true);
        mTrailersListView.setAdapter(trailerAdapter);

        mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "http://www.youtube.com/watch?v=" + trailers.get(position).getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void showReviews(final View rootView) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(mContext, reviews);
        mReviewsListView = (ExpandableHeightListView) rootView.findViewById(R.id.listview_reviews);
        mReviewsListView.setExpanded(true);
        mReviewsListView.setAdapter(reviewAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(MOVIE_KEY, selectedMovie);
        outState.putParcelableArrayList(TRAILERS_KEY, trailers);
        outState.putParcelableArrayList(REVIEWS_KEY, reviews);

        super.onSaveInstanceState(outState);
    }
}
