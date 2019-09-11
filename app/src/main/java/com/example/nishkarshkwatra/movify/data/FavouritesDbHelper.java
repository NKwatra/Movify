package com.example.nishkarshkwatra.movify.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouritesDbHelper extends SQLiteOpenHelper {

    // define constants for database name and version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="FavouriteMoviesDb";

    // constructor
    public FavouritesDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // command for creating favourites table
         final  String CREATE_TABLE_FAVOURITES_COMMAND = "CREATE TABLE " + FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME
                + " ( " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_POSTER_URI + " TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_NAME
                + " TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_AVERAGE_RATING + " REAL NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ID
                + " INTEGER NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_YEAR + "TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_SYNOPSIS
                + " TEXT NOT NULL );";

         // command for creating moviegenre table
         final String CREATE_TABLE_MOVIE_GENRE_COMMAND = "CREATE TABLE " + FavouritesDatabaseContract.MovieGenres.TABLE_NAME + "( " +
                 FavouritesDatabaseContract.MovieGenres.COLUMN_MOVIE_ID + "INTEGER NOT NULL, " + FavouritesDatabaseContract.MovieGenres.COLUMN_GENRE_ID
                 + " INTEGER NOT NULL );";

         //create the required tables and database
         db.execSQL(CREATE_TABLE_FAVOURITES_COMMAND);
         db.execSQL(CREATE_TABLE_MOVIE_GENRE_COMMAND);
    }

    // statements to be executed on upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // drop original tables and create new ones
        final String DROP_TABLE_FAVOURITES_COMMAND = "DROP TABLE IF EXISTS " + FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME + ";";
        final String DROP_TABLE_MOVIE_GENRE_COMMAND = "DROP TABLE IF EXISTS " + FavouritesDatabaseContract.MovieGenres.TABLE_NAME + ";";

        db.execSQL(DROP_TABLE_FAVOURITES_COMMAND);
        db.execSQL(DROP_TABLE_MOVIE_GENRE_COMMAND);

        // recreate new tables with updated schemas
        onCreate(db);
    }
}
