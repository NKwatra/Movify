package com.example.nishkarshkwatra.movify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.nishkarshkwatra.movify.UI.MovieDetailsFragment;
import com.example.nishkarshkwatra.movify.UI.MoviesListFragment;
import com.example.nishkarshkwatra.movify.entity.Movie;

public class DetailsActivity extends AppCompatActivity implements MovieDetailsFragment.onMovieChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // during orientation changes, a fragment is destroyed, but the system automatically creates a new
        // instance of the fragment and attaches it to parent activity, so manually attach fragment only if the
        // parent activity is created for the first time, else use the fragment attached by system automatically
        if(savedInstanceState == null) {
            // get intent that started this activity
            Intent activityStartIntent = getIntent();

            // extract movie parameters from the intent
            String name = activityStartIntent.getStringExtra(MovieDetailsFragment.MOVIE_NAME_KEY);
            String year = activityStartIntent.getStringExtra(MovieDetailsFragment.MOVIE_YEAR_KEY);
            String description = activityStartIntent.getStringExtra(MovieDetailsFragment.MOVIE_DESCRIPTION_KEY);
            double rating = activityStartIntent.getDoubleExtra(MovieDetailsFragment.MOVIE_RATING_KEY, 5.0);
            int id = activityStartIntent.getIntExtra(MovieDetailsFragment.MOVIE_ID_KEY, 1);
            int[] genres = activityStartIntent.getIntArrayExtra(MovieDetailsFragment.MOVIE_GENRES_KEY);
            String poster = activityStartIntent.getStringExtra(MovieDetailsFragment.MOVIE_POSTER_KEY);

            // create a new movie instance
            Movie movie = new Movie(poster, name, genres, id, rating, description, year);

            // create a fragment to be attached to the details activity
            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(movie, this);

            // attach the detail fragment to detail activity
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_detail_fragment_container, fragment)
                    .commit();

            this.setTitle(name);
        }
    }


    @Override
    public void onMovieChange(Movie newMovie) {

        // Create an instance for fragment for the new movie
        MovieDetailsFragment newMovieFragment = MovieDetailsFragment.newInstance(newMovie, this);

        // replace the fragment and add previous fragment to backstack of Activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_detail_fragment_container, newMovieFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }
}
