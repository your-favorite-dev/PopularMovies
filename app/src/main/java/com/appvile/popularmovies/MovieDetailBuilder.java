package com.appvile.popularmovies;

public class MovieDetailBuilder {
    private StringBuilder sb;

    protected String buildMovieDetails(MovieDetails movieDetailsObject){
        sb = new StringBuilder();
        sb.append("Title: ").append(movieDetailsObject.getTitle()).append("\n");
        sb.append("Release Date: ").append(movieDetailsObject.getReleaseDate()).append("\n");
        sb.append("User Rating: ").append(movieDetailsObject.getUserRating()).append("\n");
        return sb.toString();
    }

    protected String buildMovieOverview(MovieDetails movieDetailsObject){
        sb = new StringBuilder();
        sb.append("Overview: ").append("\n").append(movieDetailsObject.getOverview());
        return sb.toString();
    }
}
