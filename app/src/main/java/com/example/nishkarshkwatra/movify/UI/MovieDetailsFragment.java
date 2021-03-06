package com.example.nishkarshkwatra.movify.UI;


import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nishkarshkwatra.movify.BuildConfig;
import com.example.nishkarshkwatra.movify.Networking.MovieDataLoader;
import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.CastListAdapter;
import com.example.nishkarshkwatra.movify.adapter.MovieListAdapter;
import com.example.nishkarshkwatra.movify.adapter.ReviewListAdapter;
import com.example.nishkarshkwatra.movify.adapter.SimilarMoviesListAdapter;
import com.example.nishkarshkwatra.movify.data.FavouritesDatabaseContract;
import com.example.nishkarshkwatra.movify.data.JsonUtils;
import com.example.nishkarshkwatra.movify.entity.Cast;
import com.example.nishkarshkwatra.movify.entity.Movie;
import com.example.nishkarshkwatra.movify.entity.Review;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

public class MovieDetailsFragment extends Fragment implements YouTubePlayer.PlaybackEventListener, CastListAdapter.onItemClickListener,
SimilarMoviesListAdapter.onItemClickListener{

    // constant keys to store movie data
    public static final String MOVIE_NAME_KEY = "name";
    public static final String MOVIE_YEAR_KEY = "year";
    public static final String MOVIE_RATING_KEY = "rating";
    public static final String MOVIE_DESCRIPTION_KEY = "synopsis";
    public static final String MOVIE_ID_KEY = "id";
    public static final String MOVIE_GENRES_KEY = "genres";
    public static final String MOVIE_POSTER_KEY = "poster";

    // constants for loader ids
    public static final int VIDEO_LOADER_ID = 10000;
    public static final int CAST_LOADER_ID = 10001;
    public static final int SIMILAR_MOVIES_LOADER_ID = 10002;
    public static final int REVIEW_LOADER_ID = 10003;

    // constant for movie page on TmDb
    public static final String MOVIE_BASE_URL = "https://www.themoviedb.org/movie/";

    // interface to be implemented by parent activity for replacing fragments
    public interface onMovieChangeListener
    {
        void onMovieChange(Movie newMovie);
        void refreshFragment();
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
    private String mMoviePoster;
    private ProgressBar mCastLoading;
    private ProgressBar mSimilarLoading;
    private ProgressBar mReviewsLoading;
    private LinearLayout mNoInternet;
    private ScrollView mContainer;

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
        args.putIntArray(MOVIE_GENRES_KEY, movie.getmMovieGenres());
        args.putString(MOVIE_POSTER_KEY, movie.getmMoviePoster());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell the system that this fragment will respond to options menu items
        setHasOptionsMenu(true);
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
        mNoInternet = (LinearLayout) view.findViewById(R.id.ll_no_connection);
        mContainer = (ScrollView) view.findViewById(R.id.sv_movie_detail_container);

        // fetch values of arguments
       final  Bundle arguments = getArguments();

        // display values of various movie paramters
        mMovieName.setText(arguments.getString(MOVIE_NAME_KEY));
        mMovieReleaseYear.setText(arguments.getString(MOVIE_YEAR_KEY));
        mMovieSynopsis.setText(arguments.getString(MOVIE_DESCRIPTION_KEY));
        mMovieAverageRating.setRating((float)arguments.getDouble(MOVIE_RATING_KEY));
        mMoviePoster = arguments.getString(MOVIE_POSTER_KEY);

        // store movie id value
        mMovieId = arguments.getInt(MOVIE_ID_KEY);

        // attach a click listener to bookmark button
        mMovieBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mMovieBookmarkButton.setImageResource(R.drawable.baseline_star_black_36);

                // convert list of genres into a string
                // Eg [1,3,2] --> 1:3:2:
                StringBuffer buffer = new StringBuffer();
                for(int num: arguments.getIntArray(MOVIE_GENRES_KEY))
                    buffer.append(num + ":");

                // obtain values of various movie attributes and store in a content values object
                ContentValues values = new ContentValues();
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_NAME, mMovieName.getText().toString());
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_AVERAGE_RATING,arguments.getDouble(MOVIE_RATING_KEY) );
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_GENRES, buffer.toString());
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_ID, mMovieId);
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_YEAR, mMovieReleaseYear.getText().toString());
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_SYNOPSIS, mMovieSynopsis.getText().toString());

                // method to download the poster image of movie
                // returns the location of poster on external storage
                String location = downloadPoster(MovieListAdapter.BASE_IMAGE_URL  + mMoviePoster);
                values.put(FavouritesDatabaseContract.FavouriteEntry.COLUMN_MOVIE_POSTER_URI, location);

                // diplay a toolbar message of movie added to favourites
                Snackbar.make(getActivity().findViewById(R.id.cl_snackbar_associate), R.string.bookmark_done_text, Snackbar.LENGTH_LONG).show();

                getContext().getContentResolver().insert(FavouritesDatabaseContract.FavouriteEntry.CONTENT_URI, values);
            }
        });


        return view;
    }

    public String downloadPoster(String posterUrl)
    {
        // obtain a reference to required directory, path would be like app's_external_directory/files/pictures/Favourite movies
        File imageDir = new File(getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "Favourite Movies");

        // create the directory if it doesn't exist
        if(!imageDir.exists())
            imageDir.mkdirs();

        // obtain a reference to Download manager
        DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        // create a uri from string
        Uri downloadUri = Uri.parse(posterUrl);

        // create a new request
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        // add request attributes
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Downloading" + mMovieName.getText())
                .setDescription("Saving the file " + mMovieName.getText() + " onto the phone")
                .setDestinationInExternalFilesDir(getContext(),Environment.DIRECTORY_PICTURES, "Favourite Movies/" + mMovieId + ".jpg" );

        // make a request to download
        manager.enqueue(request);

        return getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Favourite Movies/" + mMovieId + ".jpg";

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
                if(result == null)
                {
                    showNoNetwork();
                    return;
                }
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
                if(s == null)
                {
                    showNoNetwork();
                    return;
                }
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
                if(s == null)
                {
                    showNoNetwork();
                    return;
                }
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
                if(s == null)
                {
                    showNoNetwork();
                    return;
                }
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

        Bundle videoBundle = new Bundle();
        videoBundle.putString(MoviesListFragment.PATH_CODE, "movie/" + mMovieId + "/videos");

        // define bundle to supply to CAST_LOADER
        Bundle castBundle = new Bundle();
        castBundle.putString(MoviesListFragment.PATH_CODE, "movie/" + mMovieId + "/credits");


        // start loader to load similar movies data
        Bundle similarMoviesBundle = new Bundle();
        similarMoviesBundle.putString(MoviesListFragment.PATH_CODE, "movie/"+ mMovieId + "/similar");


        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();


        if(activeNetworkInfo !=null && activeNetworkInfo.isConnectedOrConnecting())
        {
            mNoInternet.setVisibility(View.GONE);
            getActivity().getSupportLoaderManager().restartLoader(VIDEO_LOADER_ID, videoBundle, videoCallbacks);
            // initialize cast loader
            getActivity().getSupportLoaderManager().restartLoader(CAST_LOADER_ID, castBundle, castCallbacks);
            // initialise the loader
            getActivity().getSupportLoaderManager().restartLoader(SIMILAR_MOVIES_LOADER_ID, similarMoviesBundle, similarMoviesCallback);
            getActivity().getSupportLoaderManager().restartLoader(REVIEW_LOADER_ID,reviewBundle, reviewsCallback);
        }else
        {
           showNoNetwork();
        }
    }

    public void showNoNetwork()
    {
        mNoInternet.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.INVISIBLE);
        Button tryAgainButton = mNoInternet.findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.retry_connecting, Toast.LENGTH_LONG).show();
                mMovieChangeListener.refreshFragment();
            }
        });
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

    // create option to share movie link on clicking the share button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // if share button is pressed, create share intent
        if(item.getItemId() == R.id.menu_item_share)
        {
            StringBuffer buf = new StringBuffer();
            buf.append("Hey, Checkout this awesome movie! ");
            buf.append(MOVIE_BASE_URL);
            buf.append(mMovieId);

            Intent shareMovieIntent = new Intent();
            shareMovieIntent.setAction(Intent.ACTION_SEND);
            shareMovieIntent.putExtra(Intent.EXTRA_TEXT, buf.toString());
            shareMovieIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(shareMovieIntent, "Share Movie");
            startActivity(shareIntent);
            // return true to tell android system that we are manually handling this event
            return  true;
        }
        // else let super class handle the event
        else
            return super.onOptionsItemSelected(item);
    }
}
