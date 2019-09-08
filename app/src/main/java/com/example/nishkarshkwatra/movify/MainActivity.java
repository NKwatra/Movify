package com.example.nishkarshkwatra.movify;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        mMoviesTabLayout.setupWithViewPager(mMoviesViewPager);

    }
}
