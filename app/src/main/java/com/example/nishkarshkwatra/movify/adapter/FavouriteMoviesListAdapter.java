package com.example.nishkarshkwatra.movify.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.data.FavouritesDatabaseContract;

public class FavouriteMoviesListAdapter extends RecyclerView.Adapter<FavouriteMoviesListAdapter.FavouriteMovieHolder> {

    // add cursor for data source
    private Cursor mFavouritesDataSource;

    @NonNull
    @Override
    public FavouriteMovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // inflate text view for each list item and create a view holder
        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourite_movies_list_item, viewGroup, false);
        return new FavouriteMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMovieHolder favouriteMovieHolder, int position) {

        // data exists in given position in the cursor, then update the data
        if(mFavouritesDataSource.moveToPosition(position))
        {
            favouriteMovieHolder.mFavouriteMovieTextView.setText(mFavouritesDataSource.getString(2));
        }
    }

    @Override
    public int getItemCount() {
        // return the total number of items
        if(mFavouritesDataSource ==null)
            return 0;
        else
            return mFavouritesDataSource.getCount();
    }

    public class FavouriteMovieHolder extends RecyclerView.ViewHolder
    {
        // create member variable to store reference to text view
        private TextView mFavouriteMovieTextView;

        public FavouriteMovieHolder(View view)
        {
            super(view);
            mFavouriteMovieTextView = (TextView) view.findViewById(R.id.tv_favourite_movie_list_name);
        }
    }

    public void swapCursor(Cursor newSource)
    {
        // replace cursor when new data set is available
        mFavouritesDataSource = newSource;
        notifyDataSetChanged();
    }
}
