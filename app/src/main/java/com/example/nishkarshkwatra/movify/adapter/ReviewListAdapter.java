package com.example.nishkarshkwatra.movify.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.entity.Review;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewHolder> {

    // dataset for reviews
    private ArrayList<Review> mReviewDataSet;

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        Review currentReview = mReviewDataSet.get(i);
        reviewHolder.mAuthorTextView.setText(currentReview.getmAuthor());
        reviewHolder.mContentTextView.setText(currentReview.getmReviewContent());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder
    {
        // member variables to store view references
        private TextView mAuthorTextView;
        private TextView mContentTextView;
        public ReviewHolder(View view)
        {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_movie_detail_review_author);
            mContentTextView = (TextView) view.findViewById(R.id.tv_movie_detail_review_content);
        }
    }

    public void swapDataset(ArrayList<Review> newDataset)
    {
        mReviewDataSet = newDataset;
        notifyDataSetChanged();
    }

}
