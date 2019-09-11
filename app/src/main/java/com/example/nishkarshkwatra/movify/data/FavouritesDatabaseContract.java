package com.example.nishkarshkwatra.movify.data;

import android.provider.BaseColumns;

public class FavouritesDatabaseContract {

    // private constructor to prevent instantiation of contract class
    public FavouritesDatabaseContract(){}

    // class to represent table for movies marked as favourite
    public static class FavouriteEntry implements BaseColumns
    {
        // define constants for table and column names
        public static final String TABLE_NAME = "Favourites";
        public static final String COLUMN_MOVIE_POSTER_URI = "poster";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_RELEASE_YEAR = "release";
        public static final String COLUMN_MOVIE_ID ="id";
        public static final String COLUMN_MOVIE_AVERAGE_RATING = "rating";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
    }

    // class to store multivalued attribute genre id for the movie
    public static class MovieGenres implements BaseColumns
    {
        // define constants for table and column names
        public static final String TABLE_NAME = "MovieGenres";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_GENRE_ID = "genreId";
    }
}
