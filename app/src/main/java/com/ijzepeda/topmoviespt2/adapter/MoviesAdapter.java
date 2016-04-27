package com.ijzepeda.topmoviespt2.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ijzepeda.topmoviespt2.R;
import com.ijzepeda.topmoviespt2.activity.HolderActivity;
import com.ijzepeda.topmoviespt2.activity.MovieDetailActivity;
import com.ijzepeda.topmoviespt2.fragment.MovieDetailFragment;
import com.ijzepeda.topmoviespt2.models.MovieObj;
import com.ijzepeda.topmoviespt2.util.PaletteGeneratorTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ivan.zepeda on 23/11/2015.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public final static String PAR_OBJ_KEY = "com.ijzepeda.movie.par";
    public final static String BUNDLE_OBJ_KEY = "com.ijzepeda.movie.mBundle";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    Context context;
    ArrayList<MovieObj> movieArrayList;
    OnItemClickListener mItemClickListener;

    //receive the data
    public MoviesAdapter(Context context, ArrayList<MovieObj> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;

    }

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MovieObj movie = movieArrayList.get(position);

        holder.movieTitle.setText(movie.getTitle());

        Picasso.with(context)
                .load(movie.getPoster())
                .transform(new PaletteGeneratorTransformation(100))
                .error(R.drawable.placeholder)
                .into(holder.movieImage, new PaletteGeneratorTransformation.Callback(holder.movieImage) {
                    @Override
                    public void onPalette(final Palette palette) {
                        int mutedLight;
                        mutedLight = palette.getMutedColor(context.getResources().getColor(android.R.color.black));//ContextCompat.getColor(context, R.color.colorPrimaryDark));//context.getResources().getColor(android.R.color.black));
                        holder.movieTitleHolder.setBackgroundColor(mutedLight);

                    }
                });
        holder.mainHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }


                android.support.v4.app.FragmentTransaction ft =
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                MovieDetailFragment frag = new MovieDetailFragment();
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_OBJ_KEY, movie);
//                Toast.makeText(context,"clicked on movie:"+movie.getOriginalTitle(),Toast.LENGTH_SHORT).show();
                frag.setArguments(mBundle);

                if (((HolderActivity) context).isTwoPanes()) {
                    ft.replace(R.id.movie_detail_container, frag, DETAILFRAGMENT_TAG);
                } else {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(BUNDLE_OBJ_KEY, mBundle);
                    context.startActivity(intent);
                }

                ft.addToBackStack(null);
                ft.commit();

            }
        });
    }


    @Override
    public int getItemCount() {
        return (null != movieArrayList ? movieArrayList.size() : 0);

    }

    //for some reason it never worked
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        //call async?

    }

    //MAnage the row_movie views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout mainHolder;//for ripple effect
        public LinearLayout movieTitleHolder;
        public TextView movieTitle;
        public ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mainHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            movieTitleHolder = (LinearLayout) itemView.findViewById(R.id.movieTitleHolderRipple);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieImage = (ImageView) itemView.findViewById(R.id.movieImage);
            mainHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

}