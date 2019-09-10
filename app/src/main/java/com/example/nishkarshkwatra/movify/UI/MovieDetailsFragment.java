package com.example.nishkarshkwatra.movify.UI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nishkarshkwatra.movify.BuildConfig;
import com.example.nishkarshkwatra.movify.Networking.MovieDataLoader;
import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.CastListAdapter;
import com.example.nishkarshkwatra.movify.adapter.MovieListAdapter;
import com.example.nishkarshkwatra.movify.adapter.ReviewListAdapter;
import com.example.nishkarshkwatra.movify.adapter.SimilarMoviesListAdapter;
import com.example.nishkarshkwatra.movify.data.JsonUtils;
import com.example.nishkarshkwatra.movify.entity.Cast;
import com.example.nishkarshkwatra.movify.entity.Movie;
import com.example.nishkarshkwatra.movify.entity.Review;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONException;

import java.util.ArrayList;

public class MovieDetailsFragment extends Fragment implements YouTubePlayer.PlaybackEventListener, CastListAdapter.onItemClickListener,
SimilarMoviesListAdapter.onItemClickListener{

    // constant keys to store movie data
    public static final String MOVIE_NAME_KEY = "name";
    public static final String MOVIE_YEAR_KEY = "year";
    public static final String MOVIE_RATING_KEY = "rating";
    public static final String MOVIE_DESCRIPTION_KEY = "synopsis";
    public static final String MOVIE_ID_KEY = "id";

    // constants for loader ids
    public static final int VIDEO_LOADER_ID = 10000;
    public static final int CAST_LOADER_ID = 10001;
    public static final int SIMILAR_MOVIES_LOADER_ID = 10002;
    public static final int REVIEW_LOADER_ID = 10003;

    // interface to be implemented by parent activity for repalcing fragments
    public interface onMovieChangeListener
    {
        void onMovieChange(Movie newMovie);
    }

    // store reference to onMovieChangeListener
    private onMovieChangeListener mMovieChangeListener;

    // member variables to store references
    private YouTubePlayerSupportFragment mMovieTrailer;
    private TextView mMovieName;
    private TextView mMovieReleaseYear;
    private RatingBar mMovieAverageRating;
    private ImageButton mMovieBookmarkButton;
    private TextView mMovieSynopsis;
    private RecyclerView mMovieCast;
    private RecyclerView mMovieSimilar;
    private RecyclerView mMovieReviews;
    private int mMovieId;
    private ProgressBar mCastLoading;
    private ProgressBar mSimilarLoading;
    private ProgressBar mReviewsLoading;

    // empty constructor for super class
    public MovieDetailsFragment(){}

    // Factory method to instantiate fragment
    public static MovieDetailsFragment newInstance(Movie movie, onMovieChangeListener listener)
    {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.mMovieChangeListener = listener;
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
        mMovieTrailer = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_player_fragment_movie_detail_trailer);
        mMovieName = (TextView) view.findViewById(R.id.tv_movie_detail_name);
        mMovieAverageRating = (RatingBar) view.findViewById(R.id.rb_movie_detail_rating);
        mMovieReleaseYear = (TextView) view.findViewById(R.id.tv_movie_detail_year);
        mMovieBookmarkButton = (ImageButton) view.findViewById(R.id.ib_movie_detail_bookmark);
        mMovieSynopsis = (TextView) view.findViewById(R.id.tv_movie_detail_synopsis);
        mMovieCast = (RecyclerView) view.findViewById(R.id.rv_movie_detail_cast);
        mMovieSimilar = (RecyclerView) view.findViewById(R.id.rv_movie_detail_similar_movies);
        mMovieReviews = (RecyclerView) view.findViewById(R.id.rv_movie_detail_reviews);
        mCastLoading = (ProgressBar) view.findViewById(R.id.pb_movie_detail_cast_loading);
        mSimilarLoading = (ProgressBar) view.findViewById(R.id.pb_movie_detail_similar_loading);
        mReviewsLoading = (ProgressBar) view.findViewById(R.id.pb_movie_detail_reviews_loading);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // define callbacks for videos of the movie
        LoaderManager.LoaderCallbacks<String> videoCallbacks = new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
                return new MovieDataLoader(bundle.getString(MoviesListFragment.PATH_CODE), getContext(), null, 1);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String result) {
                 String video = null;
                try
                {
                    video = JsonUtils.getMovieVideo(result);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                if(video != null)
                {
                    final String videoToBeLoaded = video;
                    // initialize the youtube player
                    mMovieTrailer.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            // set playback event listener on youtube player
                            youTubePlayer.setPlaybackEventListener(MovieDetailsFragment.this);
                            youTubePlayer.cueVideo(videoToBeLoaded);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                }else
                {
                    Toast.makeText(getContext(), "No trailer available for this movie", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {

            }
        };

        Bundle videoBundle = new Bundle();
        videoBundle.putString(MoviesListFragment.PATH_CODE, "movie/" + mMovieId + "/videos");
        getActivity().getSupportLoaderManager().restartLoader(VIDEO_LOADER_ID, videoBundle, videoCallbacks);

        // hook up the cast list recycler view with a layout manager and adapter
        mMovieCast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        final CastListAdapter castAdapter = new CastListAdapter(MovieDetailsFragment.this);
        mMovieCast.setAdapter(castAdapter);

        // set up for perfomance optimization in recycler view
        mMovieCast.setHasFixedSize(true);


        // define callbacks for loader which loads cast data
        LoaderManager.LoaderCallbacks<String> castCallbacks = new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
                return new MovieDataLoader(bundle.getString(MoviesListFragment.PATH_CODE), getContext(),mCastLoading, 1);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String s) {
                ArrayList<Cast> castList = null;
                try
                {
                    castList = JsonUtils.getCastList(s);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                mCastLoading.setVisibility(View.INVISIBLE);
                castAdapter.swapDataset(castList);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {

            }
        };


        // define bundle to supply to CAST_LOADER
        Bundle castBundle = new Bundle();
        castBundle.putString(MoviesListFragment.PATH_CODE, "movie/" + mMovieId + "/credits");

        // initialize cast holder
        getActivity().getSupportLoaderManager().restartLoader(CAST_LOADER_ID, castBundle, castCallbacks);


        // hookup similar movies recycler view with layout manager and adapter
        mMovieSimilar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mMovieSimilar.setHasFixedSize(true);
        final SimilarMoviesListAdapter similarMoviesListAdapter = new SimilarMoviesListAdapter(this);
        mMovieSimilar.setAdapter(similarMoviesListAdapter);

        // define callbacks for similar movies loader
        LoaderManager.LoaderCallbacks<String> similarMoviesCallback = new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
                return new MovieDataLoader(bundle.getString(MoviesListFragment.PATH_CODE), getContext(), mSimilarLoading, 1);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String s) {
                ArrayList<Movie> similarMoviesList = null;
                try
                {
                    similarMoviesList = JsonUtils.getMoviesList(s);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                mSimilarLoading.setVisibility(View.INVISIBLE);
                similarMoviesListAdapter.swapDataset(similarMoviesList);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {

            }
        };

        // start loader to load similar movies data
        Bundle similarMoviesBundle = new Bundle();
        similarMoviesBundle.putString(MoviesListFragment.PATH_CODE, "movie/"+ mMovieId + "/similar");

        // initialise the loader
        getActivity().getSupportLoaderManager().restartLoader(SIMILAR_MOVIES_LOADER_ID, similarMoviesBundle, similarMoviesCallback);


        // hook up reviews recycler view with layout manager and adapter
        mMovieReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        final ReviewListAdapter reviewsListAdapter = new ReviewListAdapter();
        mMovieReviews.setAdapter(reviewsListAdapter);

        // define callbacks for reviews loader
        LoaderManager.LoaderCallbacks<String> reviewsCallback = new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
                return new MovieDataLoader(bundle.getString(MoviesListFragment.PATH_CODE), getContext(), mReviewsLoading, 1);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String s) {
                ArrayList<Review> reviewsList = null;
                try
                {
                    reviewsList = JsonUtils.getReviewsList(s);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                mReviewsLoading.setVisibility(View.INVISIBLE);
                reviewsListAdapter.swapDataset(reviewsList);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {

            }
        };

        // instantiate the loader
        Bundle reviewBundle = new Bundle();
        reviewBundle.putString(MoviesListFragment.PATH_CODE, "movie/" + mMovieId + "/reviews");

        getActivity().getSupportLoaderManager().restartLoader(REVIEW_LOADER_ID,reviewBundle, reviewsCallback);
    }

    @Override
    public void onPlaying() {
        // hide the bookmark button while video is being played
       mMovieBookmarkButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPaused() {
        // show the bookmark button when the video is paused
        mMovieBookmarkButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopped() {
        // show bookmark button when video is stopped
        mMovieBookmarkButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBuffering(boolean b) {
        // hide bookmark button while video is buffering
        mMovieBookmarkButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onItemClick(Cast cast) {
        CastDetailFragment fragment = CastDetailFragment.newInstance(cast.getmCastId());
        fragment.show(getActivity().getSupportFragmentManager(), "cast info");
    }

    @Override
    public void onItemClick(Movie movie) {
        mMovieChangeListener.onMovieChange(movie);
    }
}
