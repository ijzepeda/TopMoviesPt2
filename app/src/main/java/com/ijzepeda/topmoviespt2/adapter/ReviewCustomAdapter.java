package com.ijzepeda.topmoviespt2.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ijzepeda.topmoviespt2.R;
import com.ijzepeda.topmoviespt2.models.Review;

import java.util.ArrayList;


/**
 * Created by ivan.zepeda on 14/01/2016.
 */
public class ReviewCustomAdapter extends BaseAdapter { //implements View.OnClickListener


    private static LayoutInflater inflater = null;
    public Resources res;
    Review tempValues = null;
    int i = 0;
    private Activity activity;
    private ArrayList data;

    public ReviewCustomAdapter(Activity a, Resources resLocal, ArrayList d) {

        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (convertView == null) {

            row = inflater.inflate(R.layout.review_row, null);
            holder = new ViewHolder();
            holder.commentAuthor = (TextView) row.findViewById(R.id.commentAuthor);
            holder.commentTxt = (TextView) row.findViewById(R.id.commentTxt);
            row.setTag(holder);
            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();

        if (data.size() <= 0) {
            holder.commentTxt.setText(R.string.no_reviews);
            holder.commentAuthor.setVisibility(View.INVISIBLE);

        } else {
            tempValues = null;
            tempValues = (Review) data.get(position);

            holder.commentAuthor.setText(tempValues.getCommentAuthor());
            holder.commentTxt.setText(tempValues.getCommentTxt());

        }
        return row;
    }

    /***
     * Create a holder Class to contain inflated xml file elements
     ***/
    public static class ViewHolder {
        public ImageView image;
        TextView commentAuthor;
        TextView commentDate;
        TextView commentTxt;
        RatingBar commentRating;
    }
}