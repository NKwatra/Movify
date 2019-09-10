package com.example.nishkarshkwatra.movify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nishkarshkwatra.movify.UI.GenreListFragment;
import com.example.nishkarshkwatra.movify.UI.MoviesListFragment;
import com.example.nishkarshkwatra.movify.adapter.GenreListAdapter;

public class GenreActivity extends AppCompatActivity {

    public static final String GENRE_ID_INTENT_EXTRA = "genreId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        // attach a fragment if activity is started for first time
        if(savedInstanceState == null) {

            Intent activityStartingIntent = getIntent();
            int id = activityStartingIntent.getIntExtra(GENRE_ID_INTENT_EXTRA, 0);

            MoviesListFragment fragment = MoviesListFragment.newInstance(GenreListFragment.LOADER_ID, GenreListFragment.GENRE_PATH, id);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.genre_movie_fragment_container, fragment)
                    .commit();

            this.setTitle(GenreListAdapter.idToGenreMapping.get(id).toUpperCase());
        }
    }
}
