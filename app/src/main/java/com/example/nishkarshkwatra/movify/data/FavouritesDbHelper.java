package com.example.nishkarshkwatra.movify.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavouritesDbHelper extends SQLiteOpenHelper {

    // define constants for database name and version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="FavouriteMoviesDb.db";

    // constructor
    public FavouritesDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // command for creating favourites table
        final String CREATE_TABLE_FAVOURITES_COMMAND = "CREATE TABLE " + FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME
                + " ( " + FavouritesDatabaseContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_POSTER_URI + " TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_NAME
                + " TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_AVERAGE_RATING + " REAL NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ID
                + " INTEGER NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_YEAR + " TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_SYNOPSIS
                + " TEXT NOT NULL, " + FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL );";

        Log.d("FavouriteDbHeler", CREATE_TABLE_FAVOURITES_COMMAND);
        //create the required tables and database
        db.execSQL(CREATE_TABLE_FAVOURITES_COMMAND);
    }

    // statements to be executed on upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // drop original table and create new ones
        final String DROP_TABLE_FAVOURITES_COMMAND = "DROP TABLE IF EXISTS " + FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME + ";";

        db.execSQL(DROP_TABLE_FAVOURITES_COMMAND);

        // recreate new table with updated schemas
        onCreate(db);
    }
}
