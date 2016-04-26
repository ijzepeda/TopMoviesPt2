package com.ijzepeda.topmoviespt2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class HolderActivity extends AppCompatActivity implements MovListFragment.OnMovieSelectedListener {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String LISTFRAGMENT_TAG = "LFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_holder);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    public boolean isTwoPanes() {
        return mTwoPane;
    }

    @Override
    public void onMovieSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }
}
