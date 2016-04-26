package com.ijzepeda.topmoviespt2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
import java.util.List;
import java.util.Map;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int COL_WEATHER_CONDITION_ID = 9;
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private static final String[] DETAIL_COLUMNS = {
            "Title", "OriginalTitle", "ReleaseDate", "rating", "popular", "overview",
            "posterImageView", "ratingBar"
    };
    private final String FETCH_VIDEO = "video";
    private final String FETCH_REVIEW = "review";
    TextView titleTextView, originalTitle, releaseDate, rating, popular, overview;
    ImageView posterImageView;
    RatingBar ratingBar;
    LinearLayout titleholder;
    RelativeLayout relativeLayout;
    MovieObj movieObj;
    ListView reviewsListView;
    ListView videosListView;
    LinearLayout moreDetailsLayout;
    ScrollView scrollviewDetail;
    Button addFavBtn;
    private String FETCH_CRITERIA;
    private MoviesAdapter moviesAdapter;
    private ReviewCustomAdapter movieReviewsAdapter;
    private VideoCustomAdapter moviesVideosAdapter;
    private String moviesJsonStr;
    private String movieTrailersJsonStr;
    private String movieReviewsJsonStr;
    //~~ASYNCTASK
    private ArrayList<Review> movieReviewsList;
    private ArrayList<Video> movieVideoList;
    private List<Map<String, Object>> movieVideoMapList;
    private ArrayList<MovieObj> moviesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //background white and clickable
        rootView.setBackgroundColor(getResources().getColor(R.color.fab_material_white));
        rootView.setClickable(true);

        Bundle bundle = getArguments();

        movieObj = new MovieObj(
                "0123", "DummyTitle", "poster", "OriginalTitle", "ReleaseDate", "popularity", "Overview lorem itsum", "Avg"
        );


//        Split titleTextView if its too long - I didnt wanted to use a custom bar
//        if(!getSupportActionBar().isTitleTruncated()) //for some reason it always return false
        if (movieObj.getTitle().length() > 20)
            if (movieObj.getTitle().split(",|:|-", 2).length >= 2) {
                String[] newTitle = movieObj.getTitle().split("([:-;,])", 2);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(newTitle[0]);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(newTitle[1]);
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(movieObj.getTitle());
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(movieObj.getOriginalTitle());

            }


        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.movieRelativeLayout);
        titleholder = (LinearLayout) relativeLayout.findViewById(R.id.movieTitleHolder);
        titleTextView = (TextView) titleholder.findViewById(R.id.movieTitle);
        originalTitle = (TextView) titleholder.findViewById(R.id.movieOriginalTitle);

        releaseDate = (TextView) relativeLayout.findViewById(R.id.releaseDate);
        rating = (TextView) relativeLayout.findViewById(R.id.rating);
        popular = (TextView) relativeLayout.findViewById(R.id.popular);
        overview = (TextView) relativeLayout.findViewById(R.id.overview);
        posterImageView = (ImageView) relativeLayout.findViewById(R.id.moviePosterImageView);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        reviewsListView = (ListView) rootView.findViewById(R.id.reviewsListView);
        videosListView = (ListView) rootView.findViewById(R.id.videosListView);

        movieVideoList = new ArrayList<>();
        movieReviewsList = new ArrayList<>();
        movieReviewsAdapter = new ReviewCustomAdapter(getActivity(), getResources(), movieReviewsList);
        moviesVideosAdapter = new VideoCustomAdapter(getActivity(), getResources(), movieVideoList);

        moreDetailsLayout = (LinearLayout) rootView.findViewById(R.id.moreDetailsLayout);
        scrollviewDetail = (ScrollView) rootView.findViewById(R.id.scrolldetailview);

        addFavBtn = (Button) rootView.findViewById(R.id.favBtn);
        addFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovieFav();
            }
        });

        if (bundle != null) {
            movieObj = bundle.getParcelable(MoviesAdapter.PAR_OBJ_KEY);

            // having the movieobj, save instance
            // call api to retrieve movies andreviews
          /*  FetchMovieDetailsTask moviesTask = new FetchMovieDetailsTask();
            moviesTask.execute(movieObj.getId());
*/

            setMovieDetails();

            //if one is selected then load
            //if internet
            loadMoreDetails();

        } else {
            scrollviewDetail.setVisibility(View.INVISIBLE);
        }


//scrollable managing

        scrollviewDetail.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                reviewsListView.getParent().requestDisallowInterceptTouchEvent(false);
                videosListView.getParent().requestDisallowInterceptTouchEvent(false);
                //  We will have to follow above for all scrollable contents
                return false;
            }
        });
//        scrollviewDetail
        videosListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                // this will disallow the touch request for parent scroll on touch of child view
//                p_v.getParent()
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                reviewsListView.getParent().requestDisallowInterceptTouchEvent(true);
                scrollviewDetail.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        reviewsListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                // this will disallow the touch request for parent scroll on touch of child view
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                videosListView.getParent().requestDisallowInterceptTouchEvent(true);
                scrollviewDetail.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void saveMovieFav() {
        //Creating a shared preference
        Toast.makeText(getActivity(), R.string.addedFavs, Toast.LENGTH_SHORT).show();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movieObj);
        prefsEditor.putString(movieObj.getId(), json);
        prefsEditor.commit();

    }

    public void setMovieDetails() {

        titleTextView.setText(movieObj.getTitle());
        originalTitle.setText(movieObj.getOriginalTitle());
        releaseDate.setText(movieObj.getReleaseDate());

        popular.setText(movieObj.getPopularity());
        overview.setText(movieObj.getOverview());

        rating.setText("(" + movieObj.getVotedAvg() + ")");
        ratingBar.setRating(Float.parseFloat(movieObj.getVotedAvg()) / 2);
        Picasso.with(getActivity())
                .load(movieObj.getPoster())
                .transform(new PaletteGeneratorTransformation(100))
                .error(R.drawable.placeholder)
                .into(posterImageView, new PaletteGeneratorTransformation.Callback(posterImageView) {
                    @Override
                    public void onPalette(final Palette palette) {
                        int mutedLight;
                        mutedLight = palette.getMutedColor(getResources().getColor(android.R.color.black));//ContextCompat.getColor(context, R.color.colorPrimaryDark));//context.getResources().getColor(android.R.color.black));

                        /**It is a cool effect, but I was not sure to implement it or not*/
//                        android.support.v7.app.ActionBar bar = getSupportActionBar();
//                        if(bar!=null)
//                        bar.setBackgroundDrawable(new ColorDrawable(mutedLight));

                        titleholder.setBackgroundColor(mutedLight);
                    }
                });
//        if(!((HolderActivity)getActivity()).isTwoPanes()){
        if (titleholder.getVisibility() == View.GONE) {
            //;//not using in cellphone
            ((MovieDetailActivity) getActivity()).getSupportActionBar().setTitle(movieObj.getTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //to load data from the provider or DB
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                DETAIL_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // Read weather condition ID from cursor
            int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * loadMoreDetails(will fetch reviews and videos for this selected movie, only if it has internet access)
     * FETCH ON DEMAND
     */
    public void loadMoreDetails() {
        FetchMovieDetailsTask movieReviewTask = new FetchMovieDetailsTask();
        movieReviewTask.execute(FETCH_REVIEW);

    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();

        String CRITERIA = "";


        private void getMoviesDataFromJson(String moviesJsonStr) throws JSONException {

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


            final String REVIEW_ID = "id";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";
            final String REVIEW_URL = "url";


            final String VIDEO_ID = "id";
            final String VIDEO_ISO = "iso_639_1";
            final String VIDEO_KEY = "key";
            final String VIDEO_NAME = "name";
            final String VIDEO_SITE = "site";
            final String VIDEO_SIZE = "size";
            final String VIDEO_TYPE = "type";


            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(LIST);

            moviesList = new ArrayList<>();
            movieVideoList = new ArrayList<>();
            movieReviewsList = new ArrayList<>();

            for (int i = 0; i < moviesArray.length(); i++) {


                JSONObject movie = moviesArray.getJSONObject(i);
//                JSONObject movieObject = movie.getJSONObject(POSTER_PATH);//getJSONArray(POSTER_PATH).getJSONObject(0);

                //TODO THIS IS FOR REVIEW
                if (CRITERIA.equals(FETCH_REVIEW)) {
                    Review reviewTemp;
                    reviewTemp = new Review(movie.getString(REVIEW_ID),
                            movie.getString(REVIEW_AUTHOR),
                            movie.getString(REVIEW_CONTENT),
                            movie.getString(REVIEW_URL));
                    movieReviewsList.add(reviewTemp);

                }

                //TODO THIS IS FOR VIDEOS
                else if (CRITERIA.equals(FETCH_VIDEO)) {
                    Video videoTemp;
                    videoTemp = new Video(movie.getString(VIDEO_ID),
                            movie.getString(VIDEO_ISO),
                            movie.getString(VIDEO_KEY),
                            movie.getString(VIDEO_NAME),
                            movie.getString(VIDEO_SITE),
                            movie.getString(VIDEO_SIZE),
                            movie.getString(VIDEO_TYPE));
                    movieVideoList.add(videoTemp);

                } else {
                    // THIS IS FOR MOVIE
                    MovieObj movieTemp;
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


            }


        }


        @Override
        protected Void doInBackground(String... params) {
            CRITERIA = params[0];
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            moviesJsonStr = null;
            movieTrailersJsonStr = null;
            movieReviewsJsonStr = null;


            try {
                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String MOVIE_VIDEOS = "videos";
                final String MOVIE_REVIEWS = "reviews";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL + movieObj.getId()).buildUpon()    //params[0]>instead of movieobj.getid
                        .appendPath(CRITERIA.equals(FETCH_REVIEW) ? MOVIE_REVIEWS : MOVIE_VIDEOS)
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
                    // But it does make debugging a *lot* easier
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
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
                        Log.e(LOG_TAG, getActivity().getString(R.string.error_closing_stream), e);
                    }
                }
            }

            try {
                getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data.
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (CRITERIA.equals(FETCH_REVIEW)) {

//                 Log.d("~~~~~","FETCH REVIEW POSTEXECUTE, calling fetch videos");
                FetchMovieDetailsTask movieVideosTask = new FetchMovieDetailsTask();
                movieVideosTask.execute(FETCH_VIDEO);
                movieReviewsAdapter = new ReviewCustomAdapter(getActivity(), getResources(), movieReviewsList);
                movieReviewsAdapter.notifyDataSetChanged();
                reviewsListView.setAdapter(movieReviewsAdapter);

            } else if (CRITERIA.equals(FETCH_VIDEO)) {
//                 Log.d("~~~~~","FETCH VIDEO POSTEXECUTE");//12 peticiones?
                moviesVideosAdapter = new VideoCustomAdapter(getActivity(), getResources(), movieVideoList);
                moviesVideosAdapter.notifyDataSetChanged();

                videosListView.setAdapter(moviesVideosAdapter);
                setListViewHeightBasedOnChildren(videosListView);

            } else {
//                 Log.e("~~~~~","POSTEXECUTE NOTHING AT CRITERIA");
                //            moviesAdapter=new MoviesAdapter(getActivity(),moviesList);
//            moviesAdapter.notifyDataSetChanged();
//            mRecyclerView.setAdapter(moviesAdapter);

            }


            super.onPostExecute(aVoid);
        }

        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }

    }

    //~~ENDOF ASYNCTASK


}
