package com.example.android.movie_app;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Amr on 11/28/2016.
 */
public class MovieTrailer extends RealmObject{
    public MovieTrailer(){}
    @PrimaryKey
    private String movieId;
    private String key;
    private String trailerId;

    public MovieTrailer(String movieId, String key) {
        this.movieId = movieId;
        this.key = key;
    }


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }
}
