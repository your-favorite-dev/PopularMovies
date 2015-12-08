package com.appvile.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;



/**
 * Created by SHC_Group on 12/3/15.
 */
public class MoviePosterViewAdapter extends ArrayAdapter<MovieDetails> {
    private Context mContext;
    private int mResource;
    private List<MovieDetails> mPosterUrlList;

    public MoviePosterViewAdapter(Context context, int resource, List<MovieDetails> posterUrlList) {
        super(context, resource, posterUrlList);
        mContext = context;
        mResource = resource;
        mPosterUrlList = posterUrlList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.moviePoster = (ImageView) row.findViewById(R.id.image_view_movie_poster);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        String url = mPosterUrlList.get(position).getMoviePosterURL();
        Picasso.with(getContext()).load(url)
                .resize(450,550)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image).into(holder.moviePoster);
        return row;
    }
    static class ViewHolder {
        ImageView moviePoster;
    }
}
