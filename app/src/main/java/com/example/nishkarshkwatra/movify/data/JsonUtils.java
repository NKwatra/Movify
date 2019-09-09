package com.example.nishkarshkwatra.movify.data;

import com.example.nishkarshkwatra.movify.entity.Movie;

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
}
