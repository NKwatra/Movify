package com.example.nishkarshkwatra.movify.UI;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.FavouriteMoviesListAdapter;
import com.example.nishkarshkwatra.movify.data.FavouritesDatabaseContract;

public class FavouriteMoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FavouriteMoviesListAdapter.onItemClickHandler{

    public interface onMovieDetailDisplayListener
    {
        void movieDetailDisplay(int movieId, int dbId);
    }


    public static final int LOADER_ID = 300;

    private RecyclerView mFavouriteMovieRecyclerView;
    private ProgressBar mLoadingIndicator;
    private FavouriteMoviesListAdapter mMoviesAdapter;
    private onMovieDetailDisplayListener listener;

    public FavouriteMoviesListFragment()
    {
        // empty constructor
    }

    public static FavouriteMoviesListFragment newInstance(onMovieDetailDisplayListener movieDetailDisplayListener)
    {
        FavouriteMoviesListFragment fragment = new FavouriteMoviesListFragment();
        fragment.listener = movieDetailDisplayListener;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for fragment
        final View view = inflater.inflate(R.layout.favourite_movies_list_fragment, container, false);

        // obtain references to various views
        mFavouriteMovieRecyclerView = (RecyclerView) view.findViewById(R.id.rv_favourite_movies_list);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_favourite_movies_loading);

        // hook up recycler view with layout manager and adapter
        mFavouriteMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMoviesAdapter = new FavouriteMoviesListAdapter(this);
        mFavouriteMovieRecyclerView.setAdapter(mMoviesAdapter);

        // define callbacks for right swipe
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                int id = (Integer) viewHolder.itemView.getTag();
                ContentValues values = new ContentValues();
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ACTIVE, 0);
                final String selection = FavouritesDatabaseContract.FavouriteEntry._ID + "=?";
                final String[] selectionArgs = new String[] {String.valueOf(id)};
                final Uri uri = FavouritesDatabaseContract.FavouriteEntry.CONTENT_URI.buildUpon()
                        .appendPath(String.valueOf(id)).build();
                getActivity().getContentResolver().update(uri, values, selection, selectionArgs);
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null, FavouriteMoviesListFragment.this);
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.cl_favourite_movies_list_container),
                        getResources().getString(R.string.bookmark_removed_text),
                        Snackbar.LENGTH_LONG)
                        .setAction(getResources().getText(R.string.undo_text), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues reAdd = new ContentValues();
                                reAdd.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ACTIVE, 1);
                                getActivity().getContentResolver().update(uri, reAdd, selection, selectionArgs);
                                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null, FavouriteMoviesListFragment.this);
                            }
                        })
                        .addCallback(new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                if(event != DISMISS_EVENT_ACTION)
                                {
                                    getActivity().getContentResolver().delete(uri, selection, selectionArgs);
                                }
                            }
                        });
                snackbar.show();

            }
        };

        // attach a ItemTouch helper with above callbacks to recycler view
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(mFavouriteMovieRecyclerView);

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
        return new CursorLoader(getContext(),
                FavouritesDatabaseContract.FavouriteEntry.CONTENT_URI,
                new String[]{FavouritesDatabaseContract.FavouriteEntry._ID,FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ID, FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_NAME},
                FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ACTIVE + "=?",
                new String[] {String.valueOf(1)},
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

    @Override
    public void onItemClick(int movieId, int dbId) {
        listener.movieDetailDisplay(movieId, dbId);
    }
}
