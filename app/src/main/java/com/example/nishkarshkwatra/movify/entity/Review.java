package com.example.nishkarshkwatra.movify.entity;

public class Review {

    // member variables to store review attributes
    private String mAuthor;
    private String mReviewContent;

    // constructor
    public Review(String author, String content)
    {
        mAuthor = author;
        mReviewContent = content;
    }

    //getters

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmReviewContent() {
        return mReviewContent;
    }
}
