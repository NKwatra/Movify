package com.example.nishkarshkwatra.movify.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    // define interface for click events
    public interface onItemClickHandler
    {
        void onItemClick(Movie movie);
    }

    // member variable for data source of the adapter
    private ArrayList<Movie> mMoviesDataset;
    private static onItemClickHandler mItemClickHandler;

    // Base Url for loading images
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/";

    // View holder class
    public  class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // member variables to catch reference to component views
        private ImageView mMoviePoster;
        private TextView mMovieName;
        private TextView mMovieGenres;

        // constructor to catch reference
        public MovieHolder(View view)
        {
            super(view);
            mMoviePoster = (ImageView)view.findViewById(R.id.iv_movie_poster);
            mMovieName = (TextView) view.findViewById(R.id.tv_movie_name);
            mMovieGenres = (TextView) view.findViewById(R.id.tv_movie_genres);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickHandler.onItemClick(mMoviesDataset.get(getAdapterPosition()));
        }
    }

    public MovieListAdapter(onItemClickHandler handler)
    {
        mItemClickHandler = handler;
    }

    // Inflate the layout for each view holder
    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        // Inflate the layout using LayoutInflater
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);

        // Return an instance of view holder
        return new MovieHolder(view);
    }

    // Bind the data to view holder
    @Override
    public void onBindViewHolder(@NonNull MovieHolder movieHolder, int position) {

        // Get data for current position from data source
        Movie currentMovie = mMoviesDataset.get(position);

        // Load image using picasso library
        Picasso.get().load(BASE_IMAGE_URL + currentMovie.getmMoviePoster()).into(movieHolder.mMoviePoster);

        // set the movie name and genres for given list item
        movieHolder.mMovieName.setText(currentMovie.getmMovieName());
        movieHolder.mMovieGenres.setText(Arrays.toString(currentMovie.getmMovieGenres()));

        Log.d("MovieListAdapter", this.toString());
    }

    // Return the number of items in the list
    @Override
    public int getItemCount() {

        // if data is null, return 0 else length of dataset
        if(mMoviesDataset == null)
            return 0;
        else
            return mMoviesDataset.size();
    }

    // Function to swap data set with new data, when new data loads
    public void swapDataset(ArrayList<Movie> newDataset)
    {
        if(mMoviesDataset == null)
            mMoviesDataset = newDataset;
        else if(newDataset != null && !mMoviesDataset.containsAll(newDataset))
            mMoviesDataset.addAll(newDataset);
        else
            return;
        notifyDataSetChanged();
    }
}
