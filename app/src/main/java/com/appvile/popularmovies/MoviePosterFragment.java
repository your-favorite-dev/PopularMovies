package com.appvile.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {
    @Bind(R.id.gridView)
    protected GridView posterGrid;
    private String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private MoviePosterViewAdapter moviePosterAdapter;
    private List<MovieDetails> posterUrlList = new ArrayList<>();
    private int urlPage = 1;

    public MoviePosterFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.refresh){
            urlPage = 1;
            moviePosterAdapter.clear();
            getMoviePosters();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(LOG_TAG, "Current API page loaded: " + urlPage);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getMoviePosters();
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
                    getMoviePosters();
                }
            }
        });

        return view;
    }
    private void getMoviePosters(){
        ApiClient.ApiInterface service = ApiClient.getClient();
        Call<JsonMovies> call = service.getMovieDetails(loadApiKey(), getPreference(), "US", "20",
                Integer.toString(urlPage));

        call.enqueue(new Callback<JsonMovies>() {
            @Override
            public void onResponse(Call<JsonMovies> call, Response<JsonMovies> response) {

                if (response.isSuccessful()) {
                    JsonMovies jsonMovies = response.body();
                    Log.i(LOG_TAG, "Response: " + new Gson().toJson(jsonMovies));
                    posterUrlList = jsonMovies.getMovieDetailsList();
                    moviePosterAdapter.addAll(posterUrlList);
                }
            }

            @Override
            public void onFailure(Call<JsonMovies> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }

        });
    }
    private String getPreference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String orderPreference = sharedPreferences.getString(getContext().getString(R.string.pref_order_by_key),
                getContext().getString(R.string.pref_order_by_default));
        Log.i(LOG_TAG, "Preference value from settings: " + orderPreference);
        if ("1".equals(orderPreference)) {
            return "popularity.desc";
        } else {
            return "vote_average.desc";
        }
    }
    private String loadApiKey() {
        String apiKeyFile = "movie_api.key";
        String apiKey = null;
        InputStream is = null;
        try {
            is = getContext().getAssets().open(apiKeyFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while (reader.ready()) {
                apiKey = reader.readLine();
            }
        } catch (IOException e) {
            Log.i(LOG_TAG, "The API file is empty or doesn't exist");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return apiKey;
    }
}
