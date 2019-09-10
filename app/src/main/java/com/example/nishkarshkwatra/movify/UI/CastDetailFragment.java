package com.example.nishkarshkwatra.movify.UI;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nishkarshkwatra.movify.Networking.MovieDataLoader;
import com.example.nishkarshkwatra.movify.R;
import com.example.nishkarshkwatra.movify.adapter.MovieListAdapter;
import com.example.nishkarshkwatra.movify.data.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastDetailFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<String> {

    // member variables for storing references to cast detail views
    private CircleImageView mCastImageCircleImageView;
    private TextView mCastNameTextView;
    private TextView mCastDetailTextView;
    private ImageButton mCloseImageButton;
    private ProgressBar mCastLoadingProgressBar;
    private int mPersonId;

    // constant for loader id
    public static final int CAST_DETAIL_LOADER_ID = 5000;
    public static final String CAST_ID_CODE = "castId";

    // empty constructor
    public CastDetailFragment(){}


    // factory method to instantiate fragment
    public static CastDetailFragment newInstance(int id)
    {
        CastDetailFragment fragment = new CastDetailFragment();
        Bundle args = new Bundle();
        args.putInt(CAST_ID_CODE, id);
        fragment.setArguments(args);
        return fragment;
    }

    // inflate xml resource layout for the fragment

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.cast_detail_fragment, container, false);

        // store references to views
        mCastImageCircleImageView = (CircleImageView) view.findViewById(R.id.iv_cast_detail_image);
        mCastNameTextView = (TextView) view.findViewById(R.id.tv_cast_detail_name);
        mCastDetailTextView = (TextView) view.findViewById(R.id.tv_cast_detail_description);
        mCloseImageButton = (ImageButton) view.findViewById(R.id.ib_cast_detail_close_button);
        mCastLoadingProgressBar = (ProgressBar) view.findViewById(R.id.pb_cast_detail_loading_indicator);

        // close the fragment on click of close button
        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPersonId = getArguments().getInt(CAST_ID_CODE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putString(MoviesListFragment.PATH_CODE, "person/" + mPersonId);

        getActivity().getSupportLoaderManager().restartLoader(CAST_DETAIL_LOADER_ID, bundle, this);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new MovieDataLoader(bundle.getString(MoviesListFragment.PATH_CODE), getContext(),mCastLoadingProgressBar , 1);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        String[] castDetails = null;
        try
        {
            castDetails = JsonUtils.getCastDetails(s);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        mCastLoadingProgressBar.setVisibility(View.INVISIBLE);
        Picasso.get().load(MovieListAdapter.BASE_IMAGE_URL + castDetails[0]).into(mCastImageCircleImageView);
        mCastNameTextView.setText(castDetails[1]);
        if(castDetails[2].equals(""))
            castDetails[2] = "No Information Available";
        mCastDetailTextView.setText(castDetails[2]);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

}
