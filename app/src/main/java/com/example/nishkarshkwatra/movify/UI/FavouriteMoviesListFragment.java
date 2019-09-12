package com.example.nishkarshkwatra.movify.UI;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.FavouriteMoviesListAdapter;
import com.example.nishkarshkwatra.movify.data.FavouritesDatabaseContract;

public class FavouriteMoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 300;

    private RecyclerView mFavouriteMovieRecyclerView;
    private ProgressBar mLoadingIndicator;
    private FavouriteMoviesListAdapter mMoviesAdapter;

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

        // hook up recycler view with layout manager and adapter
        mFavouriteMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMoviesAdapter = new FavouriteMoviesListAdapter();
        mFavouriteMovieRecyclerView.setAdapter(mMoviesAdapter);

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(getContext(), FavouritesDatabaseContract.FavouriteEntry.CONTENT_URI, new String[]{FavouritesDatabaseContract.FavouriteEntry._ID,
                FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ID, FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_NAME}, null, null,
                FavouritesDatabaseContract.FavouriteEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
