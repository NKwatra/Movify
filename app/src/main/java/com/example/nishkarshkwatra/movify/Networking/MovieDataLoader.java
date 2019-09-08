package com.example.nishkarshkwatra.movify.Networking;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;

public class MovieDataLoader extends AsyncTaskLoader<String> {

    private String mPath;
    private String mResultCache;
    private ProgressBar mLoadingIndicator;

    // Constructor
    // @Param path: The path of request url to query
    // @Param context: A context object(activity) to which loader needs to be associated
    // @Param loadingIndicator: a progress bar to show when loading data
    public MovieDataLoader(String path, Context context, ProgressBar loadingIndicator)
    {
        super(context);
        mPath = path;
        mLoadingIndicator = loadingIndicator;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // show the progress bar when loading begins
        mLoadingIndicator.setVisibility(View.VISIBLE);

        // directly deliver the result if loader has cached result else force a load
        if(mResultCache != null)
            deliverResult(mResultCache);
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        // Return the raw JSON response from the API.
        return NetworkUtils.getHttpsResponse(mPath);
    }

    @Override
    public void deliverResult(@Nullable String data) {
        // Cache the result before delivering it
        mResultCache = data;
        super.deliverResult(data);
    }
}