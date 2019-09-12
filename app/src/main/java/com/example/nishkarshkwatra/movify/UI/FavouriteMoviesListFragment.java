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

public class FavouriteMoviesListFragment extends Fragment {

    private RecyclerView mFavouriteMovieRecyclerView;
    private ProgressBar mLoadingIndicator;

    public FavouriteMoviesListFragment()
    {
        // empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for fragment
        View view = inflater.inflate(R.layout.favourite_movies_list_fragment, container, false);

        // obtain references to various views
        mFavouriteMovieRecyclerView = (RecyclerView) view.findViewById(R.id.rv_favourite_movies_list);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_favourite_movies_loading);

        return  view;
    }


}
