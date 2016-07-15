package com.example.shivam.popularmovies2.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by shivam on 12/07/16.
 */
public interface FvMovieReviewsColumns {

    @DataType(DataType.Type.TEXT) @PrimaryKey
    String reviewId = "id";

    @DataType(DataType.Type.TEXT) @NotNull
    String authorName = "author";

    @DataType(DataType.Type.TEXT) @NotNull
    String reviewContent = "content";

    @DataType(DataType.Type.TEXT) @NotNull  @References(table = FvMovieDatabase.MOVIEDB, column = FvMovieColumns.movieId)
    String movieId = "mid";
}
