package com.example.nishkarshkwatra.movify.UI;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.GenreListAdapter;
import com.example.nishkarshkwatra.movify.adapter.MovieListAdapter;
import com.example.nishkarshkwatra.movify.data.FavouritesDatabaseContract;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.io.File;


public class FavouriteMovieDetailFragment extends Fragment {

    // keys for arguments of the fragment
    public static final String MOVIE_ID_KEY = "movieId";
    public static final String DB_ID_KEY = "dbId";

    // constants for loader ids
    public static final int DB_DATA_LOADER_ID = 800;

        // empty constructor
    public FavouriteMovieDetailFragment(){}

    // store references to views
    private ImageView mMoviePoster;
    private TextView mMovieYear;
    private TextView mMovieName;
    private RatingBar mMovieRating;
    private YouTubePlayerSupportFragment mMovieTrailer;
    private TextView mMovieDescription;
    private TextView mMovieGenres;

    // factory method to get new instance
    public static FavouriteMovieDetailFragment newInstance(int movieId, int dbId)
    {
        FavouriteMovieDetailFragment fragment = new FavouriteMovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_KEY, movieId);
        bundle.putInt(DB_ID_KEY, dbId);
        fragment.setArguments(bundle);
        return fragment;
    }


    // overide method to obtain references to component views
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite_movie_detail_fragment, container, false);

        mMoviePoster = (ImageView) rootView.findViewById(R.id.iv_favourite_movie_detail_poster);
        mMovieYear = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_year);
        mMovieName = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_name);
        mMovieDescription = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_synopsis);
        mMovieRating = (RatingBar) rootView.findViewById(R.id.rb_favourite_movie_detail_rating);
        mMovieTrailer = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_player_favourite_movie_detail_trailer);
        mMovieGenres = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_genre);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       Bundle args = getArguments();
       final int movieId = args.getInt(MOVIE_ID_KEY);
       final int dbId = args.getInt(DB_ID_KEY);

        LoaderManager.LoaderCallbacks<Cursor> movieDataCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
                Uri uri = FavouritesDatabaseContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(dbId)).build();
                return new CursorLoader(getContext(),uri, null, null, null, null);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                cursor.moveToFirst();
                mMovieName.setText(cursor.getString(cursor.getColumnIndex(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_NAME)));
                mMovieYear.setText(cursor.getString(cursor.getColumnIndex(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_YEAR)));
                mMovieRating.setRating(cursor.getFloat(cursor.getColumnIndex(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_AVERAGE_RATING)));

                File imageFile = new File(cursor.getString(cursor.getColumnIndex(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_POSTER_URI)));
                mMovieDescription.setText(cursor.getString(cursor.getColumnIndex(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_SYNOPSIS)));

                String[] genres = cursor.getString(cursor.getColumnIndex(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_GENRES))
                        .split(":");
                StringBuffer genresBuf = new StringBuffer();
                for(String genre: genres)
                {
                    genresBuf.append(GenreListAdapter.idToGenreMapping.get(Integer.parseInt(genre)));
                    genresBuf.append(", ");
                }

                genresBuf.delete(genresBuf.length() - 2, genresBuf.length());
                mMovieGenres.setText(genresBuf.toString());
                Picasso.get().load(imageFile).into(mMoviePoster);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        };

        getActivity().getSupportLoaderManager().restartLoader(DB_DATA_LOADER_ID, null, movieDataCallbacks);
    }
}
