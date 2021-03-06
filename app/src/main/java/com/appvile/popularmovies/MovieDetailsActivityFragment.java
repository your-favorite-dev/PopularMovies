package com.appvile.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
            if (movie != null) {
                MovieDetailBuilder detailBuilder = new MovieDetailBuilder();
                ButterKnife.bind(this, view);
                movieDetails.setText(detailBuilder.buildMovieDetails(movie));
                if (movie.getOverview() == null) {
                    movie.setOverview("No summary available");
                }
                summary.setText(detailBuilder.buildMovieOverview(movie));
                Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/"+movie.getPoster_path())
                        .resize(250, 300)
                        .centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.broken_image).into(thumbnail);
                Log.e(MovieDetailsActivityFragment.class.getSimpleName(), "Poster path: " + movie.getPoster_path());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
