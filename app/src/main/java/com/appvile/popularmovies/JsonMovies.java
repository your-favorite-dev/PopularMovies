package com.appvile.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonMovies implements Parcelable{

    private String page;
    @SerializedName("total_pages")
    private String totalPages;
    @SerializedName("totalResults")
    private String totalResults;
    @SerializedName("results")
    private List<MovieDetails> movieDetailsList;

    protected JsonMovies(Parcel in){
        page = in.readString();
        totalPages = in.readString();
        totalResults = in.readString();
        in.readList(movieDetailsList, this.getClass().getClassLoader());
    }

    public List<MovieDetails> getMovieDetailsList() {

        return movieDetailsList;
    }

    public void setMovieDetailsList(List<MovieDetails> movieDetailsList) {
        this.movieDetailsList = movieDetailsList;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(page);
        dest.writeString(totalPages);
        dest.writeString(totalResults);
        dest.writeList(movieDetailsList);
    }

    public static final Creator<JsonMovies> CREATOR = new Creator<JsonMovies>() {
        @Override
        public JsonMovies createFromParcel(Parcel in) {
            return new JsonMovies(in);
        }

        @Override
        public JsonMovies[] newArray(int size) {
            return new JsonMovies[size];
        }
    };
}
