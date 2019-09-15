package com.example.nishkarshkwatra.movify.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouritesDatabaseContract {

    // public constants for Uri
    public static final String AUTHORITY = "com.example.nishkarshkwatra.movify";

    // define base content uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // define paths
    public static final String PATH_MOVIES = "Favourites";

    // private constructor to prevent instantiation of contract class
    public FavouritesDatabaseContract(){}

    // class to represent table for movies marked as favourite
    public static class FavouriteEntry implements BaseColumns
    {
        // content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();

        // define constants for table and column names
        public static final String TABLE_NAME = "Favourites";
        public static final String COLUMN_MOVIE_POSTER_URI = "poster";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_RELEASE_YEAR = "release";
        public static final String COLUMN_MOVIE_ID ="id";
        public static final String COLUMN_MOVIE_AVERAGE_RATING = "rating";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
        public static final String COLUMN_MOVIE_GENRES = "genres";
        public static final String COLUMN_MOVIE_ACTIVE = "isActive";
    }

}
