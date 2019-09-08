package com.example.nishkarshkwatra.movify.Networking;

import android.net.Uri;
import android.util.Log;

import com.example.nishkarshkwatra.movify.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    // Define constants required for fetching data
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_PAGE = "page";
    public static final String VALUE_LANGUAGE = "en-US";
    public static final String VALUE_PAGE = "1";

    // method to create desired url for given path
    // @Param path : The path segment of api which needs to be queried
    // Eg movie/popular, movie/top_rated etc.
    public static URL createDataUrl(String path)
    {
        // Build a uri with path and query params
        Uri sourceUri = Uri.parse(BASE_URL + path)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, VALUE_LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, VALUE_PAGE)
                .build();

        // convert the uri to url
        URL dataUrl = null;
        try {
            dataUrl = new URL(sourceUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }finally {
            Log.d("Network Utils", sourceUri.toString());
            return dataUrl;
        }
    }

    // method to fetch response from the TmDb api
    public static String getHttpsResponse(String path)
    {
        // query the given url for data
        URL dataURL = createDataUrl(path);
        HttpsURLConnection connection = null;
        String response = null;
        try
        {
            // open connection to the url
            connection = (HttpsURLConnection) dataURL.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            // get the input stream and extract characters from it
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\A");

            if(scanner.hasNext())
                response = scanner.next();
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            connection.disconnect();
            Log.d("Network Utils", response);
            return response;
        }
    }
}
