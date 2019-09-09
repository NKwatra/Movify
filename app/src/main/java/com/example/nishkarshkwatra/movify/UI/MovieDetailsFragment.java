package com.example.nishkarshkwatra.movify.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.entity.Movie;

public class MovieDetailsFragment extends Fragment {

    // constant keys to store movie data
    public static final String MOVIE_NAME_KEY = "name";
    public static final String MOVIE_YEAR_KEY = "year";
    public static final String MOVIE_RATING_KEY = "rating";
    public static final String MOVIE_DESCRIPTION_KEY = "synopsis";
    public static final String MOVIE_ID_KEY = "id";

    // member variables to store references
    private VideoView mMovieTrailer;
    private TextView mMovieName;
    private TextView mMovieReleaseYear;
    private RatingBar mMovieAverageRating;
    private ImageButton mMovieBookmarkButton;
    private TextView mMovieSynopsis;
    private RecyclerView mMovieCast;
    private RecyclerView mMovieSimilar;
    private RecyclerView mMovieReviews;
    private int mMovieId;

    // empty constructor for super class
    public MovieDetailsFragment(){}

    // Factory method to instantiate fragment
    public static MovieDetailsFragment newInstance(Movie movie)
    {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(MOVIE_NAME_KEY,movie.getmMovieName());
        args.putString(MOVIE_YEAR_KEY, movie.getmMovieReleaseYear());
        args.putDouble(MOVIE_RATING_KEY, movie.getmMovieAverageRating());
        args.putString(MOVIE_DESCRIPTION_KEY, movie.getmMovieSynopsis());
        args.putInt(MOVIE_ID_KEY, movie.getmMovieId());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        // store reference to various views in the layout
        mMovieTrailer = (VideoView) view.findViewById(R.id.vv_movie_detail_trailer);
        mMovieName = (TextView) view.findViewById(R.id.tv_movie_detail_name);
        mMovieAverageRating = (RatingBar) view.findViewById(R.id.rb_movie_detail_rating);
        mMovieReleaseYear = (TextView) view.findViewById(R.id.tv_movie_detail_year);
        mMovieBookmarkButton = (ImageButton) view.findViewById(R.id.ib_movie_detail_bookmark);
        mMovieSynopsis = (TextView) view.findViewById(R.id.tv_movie_detail_synopsis);
        mMovieCast = (RecyclerView) view.findViewById(R.id.rv_movie_detail_cast);
        mMovieSimilar = (RecyclerView) view.findViewById(R.id.rv_movie_detail_similar_movies);
        mMovieReviews = (RecyclerView) view.findViewById(R.id.rv_movie_detail_reviews);

        // fetch values of arguments
        Bundle arguments = getArguments();

        // display values of various movie paramters
        mMovieName.setText(arguments.getString(MOVIE_NAME_KEY));
        mMovieReleaseYear.setText(arguments.getString(MOVIE_YEAR_KEY));
        mMovieSynopsis.setText(arguments.getString(MOVIE_DESCRIPTION_KEY));
        mMovieAverageRating.setRating((float)arguments.getDouble(MOVIE_RATING_KEY));

        // store movie id value
        mMovieId = arguments.getInt(MOVIE_ID_KEY);

        return view;
    }
}
