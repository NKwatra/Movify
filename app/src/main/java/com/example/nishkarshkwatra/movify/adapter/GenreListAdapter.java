package com.example.nishkarshkwatra.movify.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.GenreHolder> {

    // hash map for genre name to genre id conversion
    public static HashMap<String, Integer> genreToIdMapping = new HashMap<>();

    // hash map for id to genre conversion
    public static HashMap<Integer, String> idToGenreMapping;

    static
    {
        // define the key value pairs of hash map
        genreToIdMapping.put("Action", 28);
        genreToIdMapping.put("Adventure", 12);
        genreToIdMapping.put("Animation", 16);
        genreToIdMapping.put("Comedy", 35);
        genreToIdMapping.put("Crime", 80);
        genreToIdMapping.put("Documentary", 99);
        genreToIdMapping.put("Drama", 18);
        genreToIdMapping.put("Family", 10751);
        genreToIdMapping.put("Fantasy", 14);
        genreToIdMapping.put("History", 36);
        genreToIdMapping.put("Horror", 27);
        genreToIdMapping.put("Music", 10402);
        genreToIdMapping.put("Mystery", 9648);
        genreToIdMapping.put("Romance", 10749);
        genreToIdMapping.put("Science Fiction", 878);
        genreToIdMapping.put("TV Movie", 10770);
        genreToIdMapping.put("Thriller", 53);
        genreToIdMapping.put("War", 10752);
        genreToIdMapping.put("Western", 37);

        // create a hash map with keys and values of above interchanged
        idToGenreMapping = new HashMap<>();
        for(Map.Entry<String, Integer> entry: genreToIdMapping.entrySet())
        {
            idToGenreMapping.put(entry.getValue(), entry.getKey());
        }
    };

    // interface for handling click events
    public interface onItemClickListener
    {
        void onItemClick(int id);
    }

    // data source for adapter, a list of genres
    private ArrayList<String> mGenreDataset;

    // reference to click listener
    private onItemClickListener mClickListener;

    // constructor
    public GenreListAdapter(onItemClickListener listener)
    {
        mClickListener = listener;
        mGenreDataset = new ArrayList<>(genreToIdMapping.keySet());
    }

    // method to supply view holder
    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genre_list_item, viewGroup, false);
        return new GenreHolder(view);
    }

    // method to bind view holder with data
    @Override
    public void onBindViewHolder(@NonNull GenreHolder genreHolder, int i) {
        genreHolder.mGenreNameTextView.setText(mGenreDataset.get(i));
    }

    // total number of items
    @Override
    public int getItemCount() {
        return mGenreDataset.size();
    }

    // View holder class
    public class GenreHolder extends RecyclerView.ViewHolder
    {
        // reference to cache view ids
        private TextView mGenreNameTextView;

        public GenreHolder(View view)
        {
            super(view);
            mGenreNameTextView = (TextView) view.findViewById(R.id.tv_genre_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(genreToIdMapping.get(mGenreDataset.get(getAdapterPosition())));
                }
            });
        }
    }


}
