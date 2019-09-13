package com.example.nishkarshkwatra.movify.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavouriteMoviesProvider extends ContentProvider {

    // define constants corresponding to different Uri
    public static final int ALL_MOVIES_ID = 100;
    public static final int MOVIE_WITH_ID = 101;

    // define a reference to FavouritesDbHelper to access database
    private FavouritesDbHelper mFavouriteMovieDbHelper;

    // initialise the underlying data source in this method
    @Override
    public boolean onCreate() {
        // instantiate favourite db helper
        mFavouriteMovieDbHelper = new FavouritesDbHelper(getContext());
        return true;
    }

    // a static reference to the Uri matcher
    public static UriMatcher sUriMatcher = buildUriMatcher();

    // define method which creates and returns a Uri Matcher
    public static UriMatcher buildUriMatcher()
    {
        // create an empty UriMatcher
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // add Uri Matcher for all movies path
        matcher.addURI(FavouritesDatabaseContract.AUTHORITY, FavouritesDatabaseContract.PATH_MOVIES, ALL_MOVIES_ID);

        // add Uri Matcher for a particular movie path
        matcher.addURI(FavouritesDatabaseContract.AUTHORITY,FavouritesDatabaseContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,  String selection, String[] selectionArgs, String sortOrder) {

        // get a reference to readable database
        SQLiteDatabase db = mFavouriteMovieDbHelper.getReadableDatabase();

        // match the uri
        int match = sUriMatcher.match(uri);

        Cursor result = null;

        switch (match)
        {
            // case when we have to query full movies table
            case ALL_MOVIES_ID:
                result = db.query(FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // case when we have to query a particular row of the table
            case MOVIE_WITH_ID:
                String rowId = uri.getLastPathSegment();
                result = db.query(FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        FavouritesDatabaseContract.FavouriteEntry._ID + "=?",
                        new String[] {rowId},
                        null,
                        null, sortOrder);
                break;
            default: throw  new UnsupportedOperationException("Unsupported uri " + uri);
        }
        if(result != null)
            result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }



    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match  = sUriMatcher.match(uri);

        // obtain a reference to writable database
        SQLiteDatabase db = mFavouriteMovieDbHelper.getWritableDatabase();

        long id;
        // check for Uri supplied
        switch (match)
        {
            case ALL_MOVIES_ID :
                id = db.insert(FavouritesDatabaseContract.FavouriteEntry.TABLE_NAME, null, values);
                break;
            default: throw new UnsupportedOperationException("Unsupported uri" + uri);
        }

        Uri returnUri= null;

        if(id > 0)
        {
            // successfully data inserted
            returnUri = ContentUris.withAppendedId(FavouritesDatabaseContract.FavouriteEntry.CONTENT_URI, id);
        }else
        {
            throw new SQLiteException("Failed to insert row into " + uri);
        }

        // notify the content resolver that data has been updated
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

    }

    @Override
    public int delete(Uri uri,   String selection,   String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri,   ContentValues values,   String selection,   String[] selectionArgs) {
        return 0;
    }
}
