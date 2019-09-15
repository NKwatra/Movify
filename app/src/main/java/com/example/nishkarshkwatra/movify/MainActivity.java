package com.example.nishkarshkwatra.movify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.example.nishkarshkwatra.movify.adapter.MoviesPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mMoviesViewPager;
    private MoviesPagerAdapter mMoviesAdapter;
    private TabLayout mMoviesTabLayout;
    private LinearLayout mNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesViewPager = (ViewPager) findViewById(R.id.vp_tabs_container);
        mMoviesTabLayout = (TabLayout) findViewById(R.id.tl_movie_tabs);
        mMoviesAdapter = new MoviesPagerAdapter(this, getSupportFragmentManager());
        mNoInternet = (LinearLayout) findViewById(R.id.ll_no_connection);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())
        {
            mMoviesViewPager.setAdapter(mMoviesAdapter);
            mMoviesViewPager.setOffscreenPageLimit(3);
            mMoviesTabLayout.setupWithViewPager(mMoviesViewPager);
            mNoInternet.setVisibility(View.GONE);
        }else
        {
            Button tryAgainButton = (Button) findViewById(R.id.try_again_button);
            tryAgainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }


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
