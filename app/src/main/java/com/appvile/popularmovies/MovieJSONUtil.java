package com.appvile.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.util.Log;

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

public class MovieJSONUtil {
    private Context mContext;
    private String LOG_TAG = MovieJSONUtil.class.getSimpleName();

    public MovieJSONUtil(Context context) {
        mContext = context;
    }

    public synchronized List<MovieDetails> getJSONMovieData(String pageNumber) {
        String jsonMovieString = "";
        List<MovieDetails> movieDetailsList;
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            MovieDbUriUtil uriUtil = new MovieDbUriUtil(mContext);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String orderPreference = sharedPreferences.getString(mContext.getString(R.string.pref_order_by_key),
                    mContext.getString(R.string.pref_order_by_default));
            Log.i(LOG_TAG, "Preference value from settings: " + orderPreference);
            if ("1".equals(orderPreference)) {
                url = uriUtil.createPopularityUri(pageNumber);
            } else {
                url = uriUtil.createRatingsUri(pageNumber);
            }

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.i(LOG_TAG, "API Call URL: "  + url.toString());

            InputStream jsonInputStream = urlConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(jsonInputStream));
            stringBuilder.append(reader.readLine()).append("\n");
            while (reader.ready()) {
                stringBuilder.append(reader.readLine()).append("\n");
            }

            if (stringBuilder.length() > 0) {
                jsonMovieString = stringBuilder.toString();
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error retrieving movie db data", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        movieDetailsList = parseJSONString(jsonMovieString);
        createPosterUrl(movieDetailsList);

        return movieDetailsList;
    }

    private String loadApiKey() {
        String apiKeyFile = "movie_api.key";
        String apiKey = null;
        InputStream is = null;
        try {
            is = mContext.getAssets().open(apiKeyFile);
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

    private List<MovieDetails> parseJSONString(String movieStringJSON) {
        final String movieObjectKey = "results";
        JSONObject movieJSON;
        List<MovieDetails> movieDetailsList = new ArrayList<>();
        try {
            movieJSON = new JSONObject(movieStringJSON);
            JSONArray movieArray = movieJSON.getJSONArray(movieObjectKey);

            for (int i = 0; i < movieArray.length(); i++) {
                MovieDetails details = new MovieDetails(Parcel.obtain());
                JSONObject singleMovieObject = movieArray.getJSONObject(i);
                details.setTitle(singleMovieObject.getString("title"));
                details.setOverview(singleMovieObject.getString("overview"));
                details.setReleaseDate(singleMovieObject.getString("release_date"));
                details.setUserRating(singleMovieObject.getString("vote_average"));
                details.setMoviePosterURL(singleMovieObject.getString("poster_path"));

                movieDetailsList.add(details);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetailsList;
    }

    private void createPosterUrl(List<MovieDetails> movieDetailsList) {
        final String authority = "http://image.tmdb.org/";
        final String baseUrl = "t/p/w342";
        StringBuilder builder;

        for (MovieDetails details : movieDetailsList) {
            builder = new StringBuilder();
            builder.append(authority).append(baseUrl).append(details.getMoviePosterURL());
            details.setMoviePosterURL(builder.toString());
        }
    }
}
