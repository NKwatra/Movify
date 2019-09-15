package com.example.nishkarshkwatra.movify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nishkarshkwatra.movify.UI.FavouriteMovieDetailFragment;
import com.example.nishkarshkwatra.movify.UI.FavouriteMoviesListFragment;

public class FavouritesActivity extends AppCompatActivity  implements FavouriteMoviesListFragment.onMovieDetailDisplayListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        if(savedInstanceState == null) {
            // insert the fragment containing favourite movies list into the activity
            FavouriteMoviesListFragment fragment = FavouriteMoviesListFragment.newInstance(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_favourite_movies_list_container, fragment)
                    .commit();
        }
    }

    @Override
    public void movieDetailDisplay(int movieId, int dbId) {

        FavouriteMovieDetailFragment fragment = FavouriteMovieDetailFragment.newInstance(movieId, dbId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_favourite_movies_list_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
