package com.appvile.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
        ;
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        posterUrlList = new ArrayList<>();
        moviePosterAdapter = new MoviePosterViewAdapter(getContext(),
                R.layout.picture_window, posterUrlList);
        ButterKnife.bind(this, view);
        posterGrid.setAdapter(moviePosterAdapter);
        posterGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    Log.i(LOG_TAG,"Touch Scroll");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.i(LOG_TAG,"First Visible Item "+firstVisibleItem);
                Log.i(LOG_TAG,"Visible Item Count "+visibleItemCount);
                Log.i(LOG_TAG,"Total Item Count "+totalItemCount);
                int total = firstVisibleItem + visibleItemCount;
               // Log.i(LOG_TAG,"Total "+total);
                if((totalItemCount - visibleItemCount) == firstVisibleItem && firstVisibleItem !=0){
                    urlPage++;
                    new MoviePosterManager().execute(String.valueOf(urlPage));
                }
                    //int last = firstVisibleItem + totalItemCount;
                   // if(visibleItemCount < 20)
                   // new MoviePosterManager().execute(String.valueOf(urlPage));

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
            }
        }
    }
}
