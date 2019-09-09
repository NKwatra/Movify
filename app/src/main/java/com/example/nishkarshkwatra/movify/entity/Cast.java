package com.example.nishkarshkwatra.movify.entity;

public class Cast {

    //member variables to store information of cast members
    private String mCastImage;
    private String mCastName;
    private int mCastId;

    // constructor
    public Cast(String image, String name, int id)
    {
        mCastImage = image;
        mCastName = name;
        mCastId = id;
    }

    //getters


    public String getmCastImage() {
        return mCastImage;
    }

    public String getmCastName() {
        return mCastName;
    }

    public int getmCastId() {
        return mCastId;
    }
}
