package com.example.nishkarshkwatra.movify.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nishkarshkwatra.movify.R;

public class MoviesListFragment extends Fragment {

    // define constants for keys to be used in bundle
    public static final String LOADER_ID_CODE = "loader";
    public static final String PATH_CODE = "path";

    // define members to store references and keys
    private RecyclerView mMoviesListRecyclerView;
    private ProgressBar mMoviesLoadingProgressBar;
    private int mLoaderId;
    private String mPath;

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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the root layout for the fragment
        View fragmentRoot = inflater.inflate(R.layout.movie_list_fragment, container, false);

        // Get reference to various vies in the layout
        mMoviesListRecyclerView = (RecyclerView) fragmentRoot.findViewById(R.id.rv_movies_holder);
        mMoviesLoadingProgressBar = (ProgressBar) fragmentRoot.findViewById(R.id.pb_movies_loading);

        // Return the root layout of the fragment
        return fragmentRoot;
    }
}
