package com.example.nishkarshkwatra.movify.data;

import android.util.Log;

import com.example.nishkarshkwatra.movify.entity.Cast;
import com.example.nishkarshkwatra.movify.entity.Movie;
import com.example.nishkarshkwatra.movify.entity.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    // method to parse the json response and extract required information from it
    // @param  rawJson : the json response from the api
    public static ArrayList<Movie> getMoviesList(String rawJson) throws JSONException
    {
        ArrayList<Movie> response = new ArrayList<>();
        JSONObject root = new JSONObject(rawJson);
        JSONArray results = root.getJSONArray("results");
        for(int i =0; i< results.length(); i++)
        {
            JSONObject currObject = results.getJSONObject(i);
            String poster =  null;
            if(currObject.getString("poster_path").split("/").length >= 2)
                poster = currObject.getString("poster_path").split("/")[1];
            String name = currObject.getString("title");
            JSONArray genresJsonArray = currObject.getJSONArray("genre_ids");
            int[] genres = new int[genresJsonArray.length()];
            for(int j=0; j< genres.length; j++)
                genres[j] = genresJsonArray.getInt(j);
            int id = currObject.getInt("id");
            double avg_rating = currObject.getDouble("vote_average");
            String synopsis = currObject.getString("overview");
            String releaseYear = currObject.getString("release_date").split("-")[0];
            response.add(new Movie(poster, name, genres, id, avg_rating, synopsis, releaseYear));
        }
        return response;
    }

    public static String getMovieVideo(String rawJson) throws JSONException
    {
        // parse json response to search for video of type Trailer
        JSONObject root = new JSONObject(rawJson);
        JSONArray results = root.getJSONArray("results");
        String response = null;
        for(int i=0; i<results.length(); i++)
        {
            JSONObject currObject = results.getJSONObject(i);
            if(currObject.getString("type").equals("Trailer"))
            {
                return currObject.getString("key");
            }
        }

        // If no trailer found, return any video associated with movie
        if(response ==null && results.length() > 1)
            return results.getJSONObject(0).getString("key");

        // if no video found return null
        return null;
    }

    public static ArrayList<Cast> getCastList(String rawJson) throws JSONException
    {
        JSONObject root = new JSONObject(rawJson);
        JSONArray cast = root.getJSONArray("cast");
        ArrayList<Cast> response = new ArrayList<>();
        for(int i=0; i< Math.min(10, cast.length()); i++)
        {
            JSONObject currentCastObject = cast.getJSONObject(i);
            String name= currentCastObject.getString("name");
            int id = currentCastObject.getInt("id");
            String profile = currentCastObject.getString("profile_path");
            if(profile != null)
                profile = profile.replace("/", "");
            response.add(new Cast(profile, name, id));
        }
        return response;
    }

    public static String[] getCastDetails(String rawJson) throws JSONException
    {
        JSONObject root = new JSONObject(rawJson);
        String[] response = new String[3];
        response[0] = root.getString("profile_path").replace("/", "");
        response[1] = root.getString("name");
        response[2] = root.getString("biography");
        Log.d("JsonUtils", response[2]);
        return response;
    }

    public static ArrayList<Review> getReviewsList(String rawJson) throws JSONException
    {
        ArrayList<Review> response = new ArrayList<>();
        JSONObject root = new JSONObject(rawJson);
        JSONArray results = root.getJSONArray("results");
        for(int i =0; i < Math.min(10, results.length()); i++)
        {
            JSONObject currentReview = results.getJSONObject(i);
            String author = currentReview.getString("author");
            String content = currentReview.getString("content");
            response.add(new Review(author, content));
        }
        return response;
    }
}
