package com.example.android.movie_app;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Amr on 11/27/2016.
 */
public class ReviewItem extends RealmObject{
    @PrimaryKey
    private String reviewId;
    private String movieId;
    private String author;
    private String content;


    public ReviewItem(){}

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
}
