package com.example.shivam.popularmovies2.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by shivam on 12/07/16.
 */
public interface FvMovieTrailersColumns {

    @DataType(DataType.Type.TEXT) @PrimaryKey
    String trailerId = "id";

    @DataType(DataType.Type.TEXT) @NotNull
    String key = "key";

    @DataType(DataType.Type.TEXT) @NotNull
    String trailerName = "name";

    @DataType(DataType.Type.TEXT) @NotNull
    String trailerType = "type";

    @DataType(DataType.Type.TEXT) @NotNull @References(table = FvMovieDatabase.MOVIEDB, column = FvMovieColumns.movieId)
    String movieId = "mid";
}
