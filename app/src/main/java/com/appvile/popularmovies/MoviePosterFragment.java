package com.appvile.popularmovies;

import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {
    @Bind(R.id.gridView)
    protected GridView posterGrid;
    private String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private MoviePosterViewAdapter moviePosterAdapter;
    private List<MovieDetails> posterUrlList;
    private int urlPage = 1;

    public MoviePosterFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        new MoviePosterManager().execute(String.valueOf(urlPage));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG,"Current API page loaded: " + urlPage);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        posterUrlList = new ArrayList<>();
        moviePosterAdapter = new MoviePosterViewAdapter(getContext(),
                R.layout.picture_window, posterUrlList);
        ButterKnife.bind(this, view);
        posterGrid.setAdapter(moviePosterAdapter);
        posterGrid.setClickable(true);
        posterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetails movie = (MovieDetails) posterGrid.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("movieObject", movie);
                startActivity(intent);
            }
        });
        posterGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int total = totalItemCount - visibleItemCount;

                if (total == firstVisibleItem && firstVisibleItem != 0) {
                    urlPage++;
                    Log.i(LOG_TAG, "Current items loaded: " + totalItemCount);
                    new MoviePosterManager().execute(String.valueOf(urlPage));
                }
            }
        });

        return view;
    }


    protected class MoviePosterManager extends AsyncTask<String, Void, List<MovieDetails>> {

        @Override
        protected List<MovieDetails> doInBackground(String... params) {

            return new MovieJSONUtil(getContext()).getJSONMovieData(params[0]);
        }

        @Override
        protected void onPostExecute(List<MovieDetails> result) {
            if (result != null) {
                moviePosterAdapter.addAll(result);
            }else{
                Toast.makeText(getContext(),"There was an issue updating the movies", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
