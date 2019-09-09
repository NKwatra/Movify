package com.example.nishkarshkwatra.movify.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.entity.Cast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.CastHolder> {

    // data set for the adapter
    private ArrayList<Cast> mCastDataSet;

    // inflate the layout from xml and return a view holder for each item
    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cast_list_item, viewGroup, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder castHolder, int pos) {

        // get the current cast object from adapter position
        Cast currentCastObject = mCastDataSet.get(pos);

        // Load the cast image and cast name form the cast object
        Picasso.get().load(MovieListAdapter.BASE_IMAGE_URL+ currentCastObject.getmCastImage())
        .into(castHolder.mCastImageCircleImageView);

        castHolder.mCastNameTextView.setText(currentCastObject.getmCastName());
    }

    // return cast data set size or 0 as the number of items
    @Override
    public int getItemCount() {
        if(mCastDataSet != null)
            return mCastDataSet.size();
        else
            return 0;
    }

    // Implement inner class which extends RecyclerView.ViewHolder
    public class CastHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView mCastImageCircleImageView;
        private TextView mCastNameTextView;

        // constructor
        public CastHolder(View view)
        {
            super(view);
            // cache the view references
            mCastImageCircleImageView = (CircleImageView) view.findViewById(R.id.iv_movie_detail_cast_image);
            mCastNameTextView = (TextView) view.findViewById(R.id.tv_movie_detail_cast_name);
        }
    }

    // Method to update cast data set with new data
    public void swapDataset(ArrayList<Cast> newSource)
    {
        mCastDataSet = newSource;
        notifyDataSetChanged();
    }


}
