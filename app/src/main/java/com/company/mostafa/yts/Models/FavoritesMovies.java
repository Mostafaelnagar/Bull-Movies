package com.company.mostafa.yts.Models;

/**
 * Created by mostafa on 2/18/2018.
 */

public class FavoritesMovies {

    private String movie_id;
    private String title;
    private String rating;
    private String large_cover_image;

    public FavoritesMovies(String movie_id, String title, String rating, String large_cover_image) {
        this.movie_id = movie_id;
        this.title = title;
        this.rating = rating;
        this.large_cover_image = large_cover_image;
    }

    public FavoritesMovies() {

    }


    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLarge_cover_image() {
        return large_cover_image;
    }

    public void setLarge_cover_image(String large_cover_image) {
        this.large_cover_image = large_cover_image;
    }
}
