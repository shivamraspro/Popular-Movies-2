package com.example.shivam.popularmovies2.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by shivam on 06/07/16.
 */

@Database(version = FvMovieDatabase.VERSION)
public final class FvMovieDatabase {
    public static final int VERSION = 1;

    @Table(FvMovieColumns.class) public static final String MOVIEDB = "moviedb";

    @Table(FvMovieTrailersColumns.class) public static final String TRAILERDB = "trailerdb";

    @Table(FvMovieReviewsColumns.class) public static final String REVIEWDB = "reviewdb";
}
