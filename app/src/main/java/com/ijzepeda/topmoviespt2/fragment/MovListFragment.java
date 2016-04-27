package com.ijzepeda.topmoviespt2.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ijzepeda.topmoviespt2.BuildConfig;
import com.ijzepeda.topmoviespt2.R;
import com.ijzepeda.topmoviespt2.activity.HolderActivity;
import com.ijzepeda.topmoviespt2.adapter.MoviesAdapter;
import com.ijzepeda.topmoviespt2.models.MovieObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MovListFragment extends Fragment {
    final private int SORT_POPULARITY = 1;
    final private int SORT_RATING = 2;
    final private int SORT_FAV = 3;
    //Communication between frags
    OnMovieSelectedListener mCallback;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter moviesAdapter;
    private ArrayList<MovieObj> moviesList;
    private String moviesJsonStr;
    private int sortMovies;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        ((HolderActivity) getActivity()).getSupportActionBar();

        moviesList = new ArrayList<>();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewMovies);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(getActivity().getApplicationContext(), moviesList);

        updateMovies();
        return rootView;
    }

    //comm between frags

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.menuSortPopular:
//                Log.d("!!!FRAG~~~~~", "POPULAR");

                sortMovies = SORT_POPULARITY;
                updateMovies();
                break;
            case R.id.menuSortRating:
//                Log.d("!!!FRAG~~~~~", "rating");

                sortMovies = SORT_RATING;
                updateMovies();
                break;
            case R.id.menuSortFav:
                sortMovies = SORT_FAV;
                showFavorites();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFavorites() {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());

        Map<String, ?> keys = appSharedPrefs.getAll();
        moviesList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
//            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());

            Gson gson = new Gson();
            String json = appSharedPrefs.getString(entry.getKey(), "");
            MovieObj objMovie = gson.fromJson(json, MovieObj.class);
            moviesList.add(objMovie);
        }

        moviesAdapter = new MoviesAdapter(getActivity(), moviesList);
        moviesAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(moviesAdapter);

    }

    // Container Activity must implement this interface
    public interface OnMovieSelectedListener {
        void onMovieSelected(int position);
    }

    //~~ASYNCTASK
    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        private void getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String LIST = "results";
            final String ID = "id";
            final String POSTER_PATH = "poster_path";
            final String POSTER_BASEURL = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w185";
            final String POPULARITY = "popularity";
            final String TITLE = "title";
            final String ORIGINAL_TITLE = "original_title";
            final String RELEASE_DATE = "release_date";
            final String OVERVIEW = "overview";
            final String VOTED_AVG = "vote_average";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(LIST);

            moviesList = new ArrayList<>();

            for (int i = 0; i < moviesArray.length(); i++) {

                MovieObj movieTemp;
                JSONObject movie = moviesArray.getJSONObject(i);
//                JSONObject movieObject = movie.getJSONObject(POSTER_PATH);//getJSONArray(POSTER_PATH).getJSONObject(0);
                movieTemp = new MovieObj(movie.getString(ID),
                        movie.getString(TITLE),
                        POSTER_BASEURL + POSTER_SIZE + movie.getString(POSTER_PATH),
                        movie.getString(ORIGINAL_TITLE),
                        movie.getString(RELEASE_DATE),
                        movie.getString(POPULARITY),
                        movie.getString(OVERVIEW),
                        movie.getString(VOTED_AVG));
                moviesList.add(movieTemp);
            }

            switch (sortMovies) {
                case SORT_POPULARITY:
                    Collections.sort(moviesList, new Comparator<MovieObj>() {
                        public int compare(MovieObj o1, MovieObj o2) {
                            return o2.popularity.compareTo(o1.popularity);
                        }
                    });
//                Snackbar.make(getCurrentFocus(), getResources().getString(R.string.popularity), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    break;
                case SORT_RATING:
                    Collections.sort(moviesList, new Comparator<MovieObj>() {
                        public int compare(MovieObj o1, MovieObj o2) {
                            return o2.votedAvg.compareTo(o1.votedAvg);
                        }
                    });
//                Snackbar.make(getCurrentFocus(), getResources().getString(R.string.rating), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    break;
                default:
                    break;
            }
        }


        @Override
        protected Void doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            moviesJsonStr = null;


            try {
                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/popular?";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            moviesAdapter = new MoviesAdapter(getActivity(), moviesList);
            moviesAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(moviesAdapter);


            super.onPostExecute(aVoid);
        }

    }
    //~~ENDOF ASYNCTASK

}
