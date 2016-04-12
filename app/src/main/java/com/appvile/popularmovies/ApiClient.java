package com.appvile.popularmovies;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SHC_Group on 3/23/16.
 */
public class ApiClient {

    private static final String BASE_URL = "https://api.themoviedb.org";
    private static ApiInterface apiInterface;

    public static ApiInterface getClient(){
        if(apiInterface == null) {
            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(logging);


            Retrofit retroClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build();
            apiInterface = retroClient.create(ApiInterface.class);
        }
        return apiInterface;
    }


    public interface ApiInterface{
        @GET("/3/discover/movie")
        Call<JsonMovies> getMovieDetails(@Query("api_key") String apiKey,
                             @Query("sort_by") String sort,
                             @Query("certification_country") String country,
                             @Query("vote_count.gte") String voteCount,
                             @Query("page") String pageNumber);

    }
}
