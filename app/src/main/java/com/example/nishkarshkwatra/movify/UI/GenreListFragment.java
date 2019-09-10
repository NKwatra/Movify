package com.example.nishkarshkwatra.movify.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nishkarshkwatra.movify.GenreActivity;
import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.GenreListAdapter;

public class GenreListFragment extends Fragment implements GenreListAdapter.onItemClickListener{

    // constant for loader Id
    public static final int LOADER_ID = 200;

    // constant for path
    public static final String GENRE_PATH = "discover/movie";

    private RecyclerView mGenreListRecyclerView;

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

        // hook up  recycler view with layout manager and adapter
        mGenreListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mGenreListRecyclerView.setAdapter(new GenreListAdapter(this));

        // Return the root layout of the fragment
        return fragmentRoot;
    }

    @Override
    public void onItemClick(int id) {

        // create intent to launch genre activity
        Intent startGenreActivityIntent = new Intent(getContext(), GenreActivity.class);
        startGenreActivityIntent.putExtra(GenreActivity.GENRE_ID_INTENT_EXTRA, id);

        startActivity(startGenreActivityIntent);

    }
}
