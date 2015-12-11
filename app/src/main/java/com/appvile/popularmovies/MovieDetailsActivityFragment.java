package com.appvile.popularmovies;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {
    @Bind(R.id.thumb_imageView)
    ImageView thumbnail;
    @Bind(R.id.movie_details_textView)
    TextView movieDetails;
    @Bind(R.id.movie_detail_summary_textView)
    TextView summary;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movieObject")) {
            MovieDetails movie = (MovieDetails) intent.getExtras().get("movieObject");
            if(movie != null){
                StringBuilder sb = new StringBuilder();
                sb.append("Title: ").append(movie.getTitle()).append("\n");
                sb.append("Release Date: ").append(movie.getReleaseDate()).append("\n");
                sb.append("User Rating: ").append(movie.getUserRating()).append("\n");
                ButterKnife.bind(this, view);
                movieDetails.setText(sb.toString());
                if(movie.getOverview() == null){
                    movie.setOverview("No summary available");
                }
                sb = new StringBuilder();
                sb.append("Overview: ").append("\n").append(movie.getOverview());
                summary.setText(sb.toString());
                Picasso.with(getContext()).load(movie.getMoviePosterURL())
                        .resize(250,300)
                        .centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.broken_image).into(thumbnail);
            }


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
