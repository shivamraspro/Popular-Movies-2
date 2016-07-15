package com.example.shivam.popularmovies2.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by shivam on 06/07/16.
 */

@ContentProvider(authority = FvMovieProvider.AUTHORITY, database = FvMovieDatabase.class)
public class FvMovieProvider {
    public static final String AUTHORITY = "com.example.shivam.popularmovies2";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIEDB = "moviedb";
        String TRAILERDB = "trailerdb";
        String REVIEWDB = "reviewdb";
    }

    private static Uri buildUri(String ... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = FvMovieDatabase.MOVIEDB)
    public static class Moviedb {

        @ContentUri(
                path = Path.MOVIEDB,
                type = "vnd.android.cursor.dir/movie")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIEDB);

        @InexactContentUri(
                name = "fav_movie",
                path = Path.MOVIEDB + "/*",
                type = "vnd.android.cursor.item/movie",
                whereColumn = FvMovieColumns.movieId,
                pathSegment = 1)
        public static Uri withId(String id) {
            return buildUri(Path.MOVIEDB, id);
        }
    }

    @TableEndpoint(table = FvMovieDatabase.TRAILERDB)
    public static class Trailerdb {
        @ContentUri(
                path = Path.TRAILERDB,
                type ="vnd.android.cursor.dir/trailer")
        public static final Uri CONTENT_URI = buildUri(Path.TRAILERDB);

        @InexactContentUri(
                name = "trailers",
                path = Path.TRAILERDB + "/*",
                type = "vnd.android.cursor.item/trailer",
                whereColumn = FvMovieTrailersColumns.movieId,
                pathSegment = 1)
        public static Uri withMovieId(String id) {
            return buildUri(Path.TRAILERDB, id);
        }
    }

    @TableEndpoint(table = FvMovieDatabase.REVIEWDB)
    public static class Reviewdb {
        @ContentUri(
                path = Path.REVIEWDB,
                type ="vnd.android.cursor.dir/trailer")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWDB);

        @InexactContentUri(
                name = "reviews",
                path = Path.REVIEWDB + "/*",
                type = "vnd.android.cursor.item/review",
                whereColumn = FvMovieReviewsColumns.movieId,
                pathSegment = 1)
        public static Uri withMovieId(String id) {
            return buildUri(Path.REVIEWDB, id);
        }
    }
}
