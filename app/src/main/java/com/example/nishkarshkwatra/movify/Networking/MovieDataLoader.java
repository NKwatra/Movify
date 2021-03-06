package com.example.nishkarshkwatra.movify.Networking;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;

public class MovieDataLoader extends AsyncTaskLoader<String> {

    // member variables to store state of loader
    private String mPath;
    private String mResultCache;
    private ProgressBar mLoadingIndicator;
    private int mPageNo;
    private int mGenreId = 0;

    // Constructor
    // @Param path: The path of request url to query
    // @Param context: A context object(activity) to which loader needs to be associated
    // @Param loadingIndicator: a progress bar to show when loading data
    // @Param pageNo: page number to be queried
    public MovieDataLoader(String path, Context context, ProgressBar loadingIndicator, int pageNo)
    {
        super(context);
        mPath = path;
        mLoadingIndicator = loadingIndicator;
        mPageNo = pageNo;
    }

    // Overloaded constructor to handle cases where genre is supplied
    // Constructor
    // @Param path: The path of request url to query
    // @Param context: A context object(activity) to which loader needs to be associated
    // @Param loadingIndicator: a progress bar to show when loading data
    // @Param pageNo: page number to be queried
    // @Param genreId : id of genre to be queried
    public MovieDataLoader(String path, Context context, ProgressBar loadingIndicator, int pageNo, int genreId)
    {
        super(context);
        mPath = path;
        mLoadingIndicator = loadingIndicator;
        mPageNo = pageNo;
        mGenreId = genreId;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // show the progress bar when loading begins
        if(mLoadingIndicator != null)
            mLoadingIndicator.setVisibility(View.VISIBLE);

        // directly deliver the result if loader has cached result else force a load
        if(mResultCache != null)
            deliverResult(mResultCache);
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {

        // no genre supplied
        if(mGenreId == 0)
            // Return the raw JSON response from the API.
            return NetworkUtils.getHttpsResponse(mPath, mPageNo);
        else
            // Return results from a particular genre
            return NetworkUtils.getHttpsResponse(mPath, mPageNo, mGenreId);
    }

    @Override
    public void deliverResult(@Nullable String data) {
        // Cache the result before delivering it
        mResultCache = data;
        super.deliverResult(data);
    }

    public int getPageNo()
    {
        return mPageNo;
    }
}
