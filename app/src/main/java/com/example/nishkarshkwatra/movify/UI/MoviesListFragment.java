package com.example.nishkarshkwatra.movify.UI;

import android.content.Intent;
import android.content.res.Configuration;
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

    // define log tag
    public static final String LOG_TAG = "MoviesListFragment";

    // define members to store references and keys
    private RecyclerView mMoviesListRecyclerView;
    private ProgressBar mMoviesLoadingProgressBar;
    private int mLoaderId;
    private String mPath;
    private MovieListAdapter mMovieListAdapter;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        // obtain the parameters for loader and path
        mLoaderId = arguments.getInt(LOADER_ID_CODE);
        mPath = arguments.getString(PATH_CODE);

        Log.d(LOG_TAG, "on Create" + this.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(LOG_TAG, "on Create View" + this.toString());

        // Inflate the root layout for the fragment
        View fragmentRoot = inflater.inflate(R.layout.movie_list_fragment, container, false);

        // Get reference to various vies in the layout
        mMoviesListRecyclerView = (RecyclerView) fragmentRoot.findViewById(R.id.rv_movies_holder);
        mMoviesLoadingProgressBar = (ProgressBar) fragmentRoot.findViewById(R.id.pb_movies_loading);


        // Return the root layout of the fragment
        return fragmentRoot;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "on Activity Created" + this.toString());
        // start loading the data in fragment once the parent activity has been created
        getActivity().getSupportLoaderManager().initLoader(mLoaderId, null, this);

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
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new MovieDataLoader(mPath, getContext(), mMoviesLoadingProgressBar);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
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
        mMovieListAdapter.notifyDataSetChanged();
        // Hide the loading indicator
        mMoviesLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Toast.makeText(getContext(), "Loader reset" + loader.getId(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "on Pause" + this.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "on stop" + this.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "on Destroy View"  + this.toString() );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "on Destroy" + this.toString());
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent startDetailsActivity = new Intent(getContext(), DetailsActivity.class);
        startActivity(startDetailsActivity);
    }
}
