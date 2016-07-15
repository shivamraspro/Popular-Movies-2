package com.example.shivam.popularmovies2.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

/**
 * Created by shivam on 06/07/16.
 */
public interface FvMovieColumns {

    @DataType(DataType.Type.TEXT) @NotNull
    String movieId = "id";

    @DataType(DataType.Type.TEXT) @NotNull
    String posterPath = "poster_path";

    @DataType(DataType.Type.TEXT) @NotNull
    String plotSynopsis = "overview";

    @DataType(DataType.Type.TEXT) @NotNull
    String releaseDate = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    String originalTitle = "original_title";

    @DataType(DataType.Type.REAL) @NotNull
    String userRating = "vote_average";
}
