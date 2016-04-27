package com.ijzepeda.topmoviespt2.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijzepeda.topmoviespt2.R;
import com.ijzepeda.topmoviespt2.models.Video;

import java.util.ArrayList;


/**
 * Created by ivan.zepeda on 14/01/2016.
 */
public class VideoCustomAdapter extends BaseAdapter implements View.OnClickListener { //implements View.OnClickListener

    private static LayoutInflater inflater = null;
    public Resources res;
    Video tempValues = null;
    int i = 0;
    private Activity activity;
    private ArrayList data;

    public VideoCustomAdapter(Activity a, Resources resLocal, ArrayList d) {

        activity = a;
        data = d;
        res = resLocal;

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

            row = inflater.inflate(R.layout.video_row, null);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            holder.videoSite = (TextView) row.findViewById(R.id.videoTypeAtSite);

            row.setTag(holder);
            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();

        if (data.size() <= 0) {
            holder.videoSite.setText(R.string.no_videos);
            holder.image.setVisibility(View.INVISIBLE);

        } else {
            tempValues = null;
            tempValues = (Video) data.get(position);

            String videoTag = activity.getString(R.string.view) + tempValues.getVideoType() + activity.getString(R.string.at) + tempValues.getVideoSite();
            holder.videoSite.setText(videoTag);

            row.setOnClickListener(this);
        }
        return row;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + tempValues.getVideoKey()));//(Video)data.get(mPosition).getVideoKey()
        activity.startActivity(intent);

    }

    public static class ViewHolder {

        public ImageView image;
        TextView videoSite;

    }


}
