package com.example.nishkarshkwatra.movify.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class FavouriteMovieDetailFragment extends Fragment {

    // keys for arguments of the fragment
    public static final String MOVIE_ID_KEY = "movieId";
    public static final String DB_ID_KEY = "dbId";

        // empty constructor
    public FavouriteMovieDetailFragment(){}

    // store references to views
    private ImageView mMoviePoster;
    private TextView mMovieYear;
    private TextView mMovieName;
    private RatingBar mMovieRating;
    private YouTubePlayerSupportFragment mMovieTrailer;
    private TextView mMovieDescription;

    // factory method to get new instance
    public static FavouriteMovieDetailFragment newInstance(int movieId, int dbId)
    {
        FavouriteMovieDetailFragment fragment = new FavouriteMovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_KEY, movieId);
        bundle.putInt(DB_ID_KEY, dbId);
        fragment.setArguments(bundle);
        return fragment;
    }


    // overide method to obtain references to component views
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite_movie_detail_fragment, container, false);

        mMoviePoster = (ImageView) rootView.findViewById(R.id.iv_favourite_movie_detail_poster);
        mMovieYear = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_year);
        mMovieName = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_name);
        mMovieDescription = (TextView) rootView.findViewById(R.id.tv_favourite_movie_detail_synopsis);
        mMovieRating = (RatingBar) rootView.findViewById(R.id.rb_favourite_movie_detail_rating);
        mMovieTrailer = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_player_favourite_movie_detail_trailer);

        return rootView;
    }
}
