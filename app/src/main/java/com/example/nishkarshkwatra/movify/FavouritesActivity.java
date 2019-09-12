package com.example.nishkarshkwatra.movify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nishkarshkwatra.movify.UI.FavouriteMoviesListFragment;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        // insert the fragment containing favourite movies list into the activity
        FavouriteMoviesListFragment fragment = new FavouriteMoviesListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_favpurite_movies_list_container, fragment)
                .commit();
    }
}
