package com.example.nishkarshkwatra.movify.UI;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.example.nishkarshkwatra.movify.BuildConfig;
import com.example.nishkarshkwatra.movify.Networking.MovieDataLoader;
import com.example.nishkarshkwatra.movify.Networking.NetworkUtils;
import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.GenreListAdapter;
import com.example.nishkarshkwatra.movify.adapter.MovieListAdapter;
import com.example.nishkarshkwatra.movify.data.FavouritesDatabaseContract;
import com.example.nishkarshkwatra.movify.data.JsonUtils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;


public class FavouriteMovieDetailFragment extends Fragment implements YouTubePlayer.PlaybackEventListener {

    // keys for arguments of the fragment
    public static final String MOVIE_ID_KEY = "movieId";
    public static final String DB_ID_KEY = "dbId";

    // constants for loader ids
    public static final int DB_DATA_LOADER_ID = 800;
    public static final int VIDEO_LOADER_ID = 801;

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

       // define callback to load movie data
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

        // define callback to load trailer
        LoaderManager.LoaderCallbacks<String> videoCallbacks = new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
                return new MovieDataLoader("movie/" + movieId + "/videos", getContext(), null, 1);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String result) {
                String video = null;
                try
                {
                    video = JsonUtils.getMovieVideo(result);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                if(video != null)
                {
                    final String videoToBeLoaded = video;
                    // initialize the youtube player
                    mMovieTrailer.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            // set playback event listener on youtube player
                            youTubePlayer.setPlaybackEventListener(FavouriteMovieDetailFragment.this);
                            youTubePlayer.cueVideo(videoToBeLoaded);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                }else
                {
                    Toast.makeText(getContext(), "No trailer available for this movie", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {

            }
        };


        // check if user is connected to network
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();


        // start loading movie data
        getActivity().getSupportLoaderManager().restartLoader(DB_DATA_LOADER_ID, null, movieDataCallbacks);

        if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())
        {
            getActivity().getSupportLoaderManager().restartLoader(VIDEO_LOADER_ID, null, videoCallbacks);
        }else
        {
            Toast.makeText(getContext(), "Please connect to internet to load trailer!!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlaying() {
        // hide movie poster when trailer is playing
        mMoviePoster.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPaused() {
        // show movie poster when video is paused
        mMoviePoster.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopped() {
        // show movie poster when video is stopped
        mMoviePoster.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBuffering(boolean b) {
        // hide movie poster when trailer is buffering
        mMoviePoster.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSeekTo(int i) {

    }
}
