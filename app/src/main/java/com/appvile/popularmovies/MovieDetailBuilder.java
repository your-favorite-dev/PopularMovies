package com.appvile.popularmovies;

public class MovieDetailBuilder {
    private StringBuilder sb;

    protected String buildMovieDetails(MovieDetails movieDetailsObject){
        sb = new StringBuilder();
        sb.append("Title: ").append(movieDetailsObject.getTitle()).append("\n");
        sb.append("Release Date: ").append(movieDetailsObject.getRelease_date()).append("\n");
        sb.append("User Rating: ").append(movieDetailsObject.getVote_average()).append("\n");
        sb.append("Vote Count").append(movieDetailsObject.getVote_count()).append("\n");
        sb.append("Vote Average").append(movieDetailsObject.getVote_average()).append("\n");
        return sb.toString();
    }

    protected String buildMovieOverview(MovieDetails movieDetailsObject){
        sb = new StringBuilder();
        sb.append("Overview: ").append("\n").append(movieDetailsObject.getOverview());
        return sb.toString();
    }
}
