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

public class GenreListFragment extends Fragment {

    private RecyclerView mGenreListRecyclerView;
    private ProgressBar mGenreLoadingProgressBar;

    public GenreListFragment()
    {
        // empty constructor for superclass
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the root layout for the fragment
        View fragmentRoot = inflater.inflate(R.layout.movie_list_fragment, container, false);

        // Get reference to various vies in the layout
        mGenreListRecyclerView = (RecyclerView) fragmentRoot.findViewById(R.id.rv_movies_holder);
        mGenreLoadingProgressBar = (ProgressBar) fragmentRoot.findViewById(R.id.pb_movies_loading);

        // Return the root layout of the fragment
        return fragmentRoot;
    }
}