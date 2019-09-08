package com.example.nishkarshkwatra.movify.entity;

import android.support.annotation.Nullable;

public class Movie {

    // member variables to store movie data
    private String mMoviePoster;
    private String mMovieName;
    private int[] mMovieGenres;
    private int mMovieId;
    private double mMovieAverageRating;
    private String mMovieSynopsis;


    /* Constructor
     *   @Param poster: The path of movie poster
     *   @Param name: The name of the movie
     *   @Param genres: A list of genre id's to which the movie belongs
     *   @Param id: A unique id assigned to recognize movie
     *   @Param avg_rating: The average rating of the movie
     *   @Param synopsis: synopsis of the movie
     */
    public Movie(String poster, String name, int[] genres, int id, double avg_rating, String synopsis)
    {
        mMoviePoster = poster;
        mMovieName = name;
        mMovieGenres = genres;
        mMovieId = id;
        mMovieAverageRating = avg_rating;
        mMovieSynopsis = synopsis;
    }

    // override equals method to perform a deep equality check


    @Override
    public boolean equals(@Nullable Object obj) {
        Movie m = (Movie) obj;
        return (this.mMovieId == m.getmMovieId());
    }

    //getters
    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public String getmMovieName() {
        return mMovieName;
    }

    public int[] getmMovieGenres() {
        return mMovieGenres;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    public double getmMovieAverageRating() {
        return mMovieAverageRating;
    }

    public String getmMovieSynopsis() {
        return mMovieSynopsis;
    }
}
