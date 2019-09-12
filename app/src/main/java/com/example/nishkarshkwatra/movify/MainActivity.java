package com.example.nishkarshkwatra.movify;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.nishkarshkwatra.movify.adapter.MoviesPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mMoviesViewPager;
    private MoviesPagerAdapter mMoviesAdapter;
    private TabLayout mMoviesTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesViewPager = (ViewPager) findViewById(R.id.vp_tabs_container);
        mMoviesTabLayout = (TabLayout) findViewById(R.id.tl_movie_tabs);
        mMoviesAdapter = new MoviesPagerAdapter(this, getSupportFragmentManager());

        mMoviesViewPager.setAdapter(mMoviesAdapter);
        mMoviesViewPager.setOffscreenPageLimit(3);

        mMoviesTabLayout.setupWithViewPager(mMoviesViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate the options menu
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_item_favourites)
        {
            // create intent to start new activity
            Intent startFavouritesActivityIntent = new Intent(this, FavouritesActivity.class);
            startActivity(startFavouritesActivityIntent);

            // return true to tell android system we are manually handling the event
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
