package com.appvile.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDbUriUtil {
    private static final String LOG_TAG = MovieDbUriUtil.class.getSimpleName();
    private final Context mContext;
    private final String authority = "api.themoviedb.org";
    private final String country = "US";

    protected MovieDbUriUtil(Context context){
        mContext = context;
    }
    protected URL createPopularityUri(String pageNumber) throws MalformedURLException{
        final String popularity = "popularity.dsc";
        String apiKey = loadApiKey();
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendEncodedPath("3")
                .appendEncodedPath("discover")
                .appendEncodedPath("movie")
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("sort_by", popularity)
                .appendQueryParameter("certification_country", country)
                .appendQueryParameter("page", pageNumber);
        return  new URL(builder.build().toString());

    }
    protected URL createRatingsUri(String pageNumber) throws MalformedURLException{
        final String rating = "vote_average.desc";
        final String votes = "10";
        String apiKey = loadApiKey();
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendEncodedPath("3")
                .appendEncodedPath("discover")
                .appendEncodedPath("movie")
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("sort_by", rating)
                .appendQueryParameter("certification_country", country)
                .appendQueryParameter("vote_count.gte", votes)
                .appendQueryParameter("page", pageNumber);
        return  new URL(builder.build().toString());
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
}
