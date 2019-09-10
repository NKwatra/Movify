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

public class SimilarMoviesListAdapter extends RecyclerView.Adapter<SimilarMoviesListAdapter.SimilarMoviesHolder> {

    // interface for handling clicks
    public interface  onItemClickListener
    {
        void onItemClick(Movie movie);
    }

    // member variable, dataset for adapter
    private ArrayList<Movie> mSimilarMoviesDataset;

    // reference to the click listener
    private onItemClickListener onItemClickListener;

    // View holder class
    public  class SimilarMoviesHolder extends RecyclerView.ViewHolder{

        // member variables to catch reference to component views
        private ImageView mSimiliarMoviePoster;
        private TextView mSimilarMovieName;

        // constructor to catch reference
        public SimilarMoviesHolder(View view)
        {
            super(view);
            mSimiliarMoviePoster = (ImageView)view.findViewById(R.id.iv_movie_detail_similar_image);
            mSimilarMovieName = (TextView) view.findViewById(R.id.tv_movie_detail_similar_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(mSimilarMoviesDataset.get(getAdapterPosition()));
                }
            });
        }

    }

    // constructor
    public SimilarMoviesListAdapter(onItemClickListener listener)
    {
        onItemClickListener = listener;
    }

    // Inflate the layout for each view holder
    @NonNull
    @Override
    public SimilarMoviesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        // Inflate the layout using LayoutInflater
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similar_movies_list_item, viewGroup, false);

        // Return an instance of view holder
        return new SimilarMoviesHolder(view);
    }

    // Bind the data to view holder
    @Override
    public void onBindViewHolder(@NonNull SimilarMoviesHolder movieHolder, int position) {

        // Get data for current position from data source
        Movie currentMovie = mSimilarMoviesDataset.get(position);

        // Load image using picasso library
        Picasso.get().load(MovieListAdapter.BASE_IMAGE_URL + currentMovie.getmMoviePoster()).into(movieHolder.mSimiliarMoviePoster);

        // set the movie name and genres for given list item
        movieHolder.mSimilarMovieName.setText(currentMovie.getmMovieName());

    }

    // Return the number of items in the list
    @Override
    public int getItemCount() {

        // if data is null, return 0 else length of dataset
        if(mSimilarMoviesDataset == null)
            return 0;
        else
            return mSimilarMoviesDataset.size();
    }

    // Function to swap data set with new data, when new data loads
    public void swapDataset(ArrayList<Movie> newDataset)
    {
        mSimilarMoviesDataset = newDataset;
        notifyDataSetChanged();
    }
}

