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
    public static final String PARAM_GENRE = "with_genres";

    // method to create desired url for given path
    // @Param path : The path segment of api which needs to be queried
    // Eg movie/popular, movie/top_rated etc.
    // @Param pageNo: The page number to be queried from api
    public static URL createDataUrl(String path, int pageNo)
    {
        // Build a uri with path and query params
        Uri sourceUri = Uri.parse(BASE_URL + path)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, VALUE_LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, ""+ pageNo)
                .build();

        // convert the uri to url
        URL dataUrl = null;
        try {
            dataUrl = new URL(sourceUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }finally {
            return dataUrl;
        }
    }

    // overloaded method to handle queries with genre
    // method to create desired url for given path
    // @Param path : The path segment of api which needs to be queried
    // Eg movie/popular, movie/top_rated etc.
    // @Param pageNo: The page number to be queried from api
    public static URL createDataUrl(String path, int pageNo, int genreId)
    {
        // Build a uri with path and query params
        Uri sourceUri = Uri.parse(BASE_URL + path)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, VALUE_LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, ""+ pageNo)
                .appendQueryParameter(PARAM_GENRE, "" + genreId)
                .build();


        // convert the uri to url
        URL dataUrl = null;
        try {
            dataUrl = new URL(sourceUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }finally {
            return dataUrl;
        }
    }

    // method to fetch response from the TmDb api
    public static String getHttpsResponse(String path, int pageNo)
    {
        // query the given url for data
        URL dataURL = createDataUrl(path, pageNo);
        HttpsURLConnection connection = null;
        String response = null;
        try
        {
            // open connection to the url
            connection = (HttpsURLConnection) dataURL.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

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
            return response;
        }
    }

    // overloaded method to restrict search results to a particular genre
    public static String getHttpsResponse(String path, int pageNo, int genreId)
    {
        // query the given url for data
        URL dataURL = createDataUrl(path, pageNo, genreId);
        HttpsURLConnection connection = null;
        String response = null;
        try
        {
            // open connection to the url
            connection = (HttpsURLConnection) dataURL.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

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
            return response;
        }
    }
}
