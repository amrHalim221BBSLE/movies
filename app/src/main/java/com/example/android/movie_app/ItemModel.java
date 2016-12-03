package com.example.android.movie_app;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Amr on 10/22/2016.
 */
public class ItemModel extends RealmObject{

    private String originalTitle;
    private String imageViewUrl;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    @PrimaryKey
    private String movieId;
    private int type;
    private boolean favourite=false;

    public ItemModel() {

    }
    public ItemModel(String originalTitle, String imageViewUrl, String overview, String voteAverage
            , String releaseDate, String movieId) {
        this.originalTitle = originalTitle;
        this.imageViewUrl = imageViewUrl;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.movieId=movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getImageViewUrl() {
        return imageViewUrl;
    }

    public void setImageViewUrl(String imageViewUrl) {
        this.imageViewUrl = imageViewUrl;
    }

    public String getOverview() {
        return overview;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

}