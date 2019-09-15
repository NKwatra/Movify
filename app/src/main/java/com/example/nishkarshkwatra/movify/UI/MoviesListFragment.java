package com.example.nishkarshkwatra.movify.UI;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nishkarshkwatra.movify.DetailsActivity;
import com.example.nishkarshkwatra.movify.Networking.MovieDataLoader;
import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.MovieListAdapter;
import com.example.nishkarshkwatra.movify.data.JsonUtils;
import com.example.nishkarshkwatra.movify.entity.Movie;

import org.json.JSONException;

import java.util.ArrayList;

public class MoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, MovieListAdapter.onItemClickHandler {

    // define constants for keys to be used in bundle
    public static final String LOADER_ID_CODE = "loader";
    public static final String PATH_CODE = "path";
    public static final String PAGE_CODE = "page";
    public static final String GENRE_CODE = "genre";

    // define log tag
    public static final String LOG_TAG = "MoviesListFragment";

    // define members to store references and keys
    private RecyclerView mMoviesListRecyclerView;
    private ProgressBar mMoviesLoadingProgressBar;
    private int mLoaderId;
    private String mPath;
    private MovieListAdapter mMovieListAdapter;
    private int mPageNo;
    private int mGenreId = 0;
    private static boolean activityRestarted = false;

    public MoviesListFragment()
    {
        // Empty constructor for super class
    }

    // factory method to create a new instance of fragment
    public static MoviesListFragment newInstance(int loaderId, String path)
    {
        MoviesListFragment fragment = new MoviesListFragment();
        Bundle args = new Bundle();
        args.putInt(LOADER_ID_CODE, loaderId);
        args.putString(PATH_CODE, path);
        fragment.setArguments(args);
        return fragment;
    }

    // overload newInstance to fetch movies of a particular genre
    public static MoviesListFragment newInstance(int loaderId, String path, int genreId)
    {
        MoviesListFragment fragment = new MoviesListFragment();
        Bundle args = new Bundle();
        args.putInt(LOADER_ID_CODE, loaderId);
        args.putString(PATH_CODE, path);
        args.putInt(GENRE_CODE, genreId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        // obtain the parameters for loader and path
        mLoaderId = arguments.getInt(LOADER_ID_CODE);
        mPath = arguments.getString(PATH_CODE);

        // store genre Id if genre is added
        if(arguments.containsKey(GENRE_CODE))
            mGenreId = arguments.getInt(GENRE_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if(savedInstanceState != null)
        {
            mPageNo = savedInstanceState.getInt(PAGE_CODE);
        }else
        {
            mPageNo = 0;
        }

        // Inflate the root layout for the fragment
        View fragmentRoot = inflater.inflate(R.layout.movie_list_fragment, container, false);

        // Get reference to various vies in the layout
        mMoviesListRecyclerView = (RecyclerView) fragmentRoot.findViewById(R.id.rv_movies_holder);
        mMoviesLoadingProgressBar = (ProgressBar) fragmentRoot.findViewById(R.id.pb_movies_loading);

        // hook up recycler view to a grid layout
        // create 2 column grid in portrait mode and 4 column in landscape mode
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            mMoviesListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }else
        {
            mMoviesListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }


        // hook up recycler view to adapter
        mMovieListAdapter = new MovieListAdapter(this);
        mMoviesListRecyclerView.setAdapter(mMovieListAdapter);


        // Return the root layout of the fragment
        return fragmentRoot;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // start loading the data in fragment once the parent activity has been created
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_CODE, ++mPageNo);

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())
            getActivity().getSupportLoaderManager().initLoader(mLoaderId, bundle, this);
        else if(!activityRestarted)
        {
            activityRestarted = true;
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }


        // register scroll listener on recycler view and load new data whenever user scrolls to the end
        mMoviesListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // check if recycler view can scroll vertically, 1 represents downward direction, -1 upward
                if(!recyclerView.canScrollVertically(1))
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PAGE_CODE, ++mPageNo);
                    if(mPageNo > 1000)
                        return;
                    if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())
                        getActivity().getSupportLoaderManager().restartLoader(mLoaderId, bundle, MoviesListFragment.this);
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        if(mGenreId == 0)
            return new MovieDataLoader(mPath, getContext(), mMoviesLoadingProgressBar, bundle.getInt(PAGE_CODE));
        else
            return new MovieDataLoader(mPath, getContext(), mMoviesLoadingProgressBar, bundle.getInt(PAGE_CODE), mGenreId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if(s == null)
        {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
            return;
        }
        ArrayList<Movie> response =null;
        try
        {
            // parse json response to get meaningful information
            response = JsonUtils.getMoviesList(s);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        // update the adapter with newly fetched data
        mMovieListAdapter.swapDataset(response);

        Log.d("MovieListFragment", s);
        // Hide the loading indicator
        mMoviesLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onItemClick(Movie movie) {

        // start detail activity on clicking a movie
        Intent startDetailsActivity = new Intent(getContext(), DetailsActivity.class);


        // pass movie attributes to the detail activity
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_NAME_KEY, movie.getmMovieName());
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_DESCRIPTION_KEY, movie.getmMovieSynopsis());
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_YEAR_KEY, movie.getmMovieReleaseYear());
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_RATING_KEY, movie.getmMovieAverageRating());
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_ID_KEY, movie.getmMovieId());
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_GENRES_KEY,movie.getmMovieGenres());
        startDetailsActivity.putExtra(MovieDetailsFragment.MOVIE_POSTER_KEY, movie.getmMoviePoster());
        startActivity(startDetailsActivity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the page no to be loaded in the instance state
        outState.putInt(PAGE_CODE, mPageNo);
    }

    @Override
    public void onStop() {
        super.onStop();
        activityRestarted = false;
    }
}
