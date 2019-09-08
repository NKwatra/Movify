package com.example.nishkarshkwatra.movify.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.UI.MoviesListFragment;

public class MoviesPagerAdapter extends FragmentPagerAdapter {

    // constants for loader id and path
    public static final int TRENDING_LOADER_ID = 1000;
    public static final int TOP_RATED_LOADER_ID = 1001;
    public static final String TRENDING_PATH = "movie/popular";
    public static final String TOP_RATED_PATH = "movie/top_rated";

    // define member to store a reference to context
    private Context mContext;

    // constructor
    public MoviesPagerAdapter(Context context, FragmentManager manager)
    {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // return appropriate fragment instance depending on the tab
        switch (position)
        {
            case 0: return null;
            case 1: return MoviesListFragment.newInstance(TRENDING_LOADER_ID, TRENDING_PATH);
            case 2: return MoviesListFragment.newInstance(TOP_RATED_LOADER_ID, TOP_RATED_PATH);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tabName = null;
        // find title of given tab of pager from string resources
        switch (position)
        {
            case 0: tabName =  mContext.getResources().getString(R.string.movies_classification_genre);
                    break;
            case 1: tabName =  mContext.getResources().getString(R.string.movies_classification_trending);
                    break;
            case 2: tabName =  mContext.getResources().getString(R.string.movies_classification_top_rated);
                    break;
        }

        return tabName;
    }
}
