package com.ijzepeda.topmoviespt2.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ijzepeda.topmoviespt2.R;
import com.ijzepeda.topmoviespt2.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    public final static String PAR_OBJ_KEY = "com.ijzepeda.movie.par";
    public final static String BUNDLE_OBJ_KEY = "com.ijzepeda.movie.mBundle";
    public final static boolean mTwoPane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            Bundle mBundle = new Bundle();
            if (extras != null) {
                mBundle = extras.getBundle(BUNDLE_OBJ_KEY);

            }
            android.support.v4.app.FragmentTransaction ft =
                    getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            MovieDetailFragment frag = new MovieDetailFragment();
            frag.setArguments(mBundle);
            ft.replace(R.id.movie_detail_container, frag, DETAILFRAGMENT_TAG);
            ft.commit();


        }


    }


    public boolean isTwoPanes() {
        return mTwoPane;
    }
}
