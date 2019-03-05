package com.company.mostafa.yts.Models;

/**
 * Created by mostafa on 2/3/2018.
 */

public class Movies_Of_Year {
    private String movie_id;
    private String title;
    private String year;
    private String rating;
    private String runtime;
    private String genres;
    private String summary;
    private String yt_trailer_code;
    private String large_cover_image;
    private String medium_cover_image;
    private String url_torrents;
    private String quality;
    private String size;
    private String imdb_code;
    private String large_screenshot_image1;
    private String large_screenshot_image2;
    private String large_screenshot_image3;
    private String url_cast_image;
    private String cast_name;
    private String char_name;

    public Movies_Of_Year() {
    }

    public Movies_Of_Year(String movie_id, String title, String year, String rating, String runtime, String genres, String summary, String yt_trailer_code, String large_cover_image, String url_torrents, String quality, String size, String imdb_code, String large_screenshot_image1, String large_screenshot_image2, String large_screenshot_image3, String url_cast_image, String cast_name, String char_name) {
        this.movie_id = movie_id;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.runtime = runtime;
        this.genres = genres;
        this.summary = summary;
        this.yt_trailer_code = yt_trailer_code;
        this.large_cover_image = large_cover_image;
        this.url_torrents = url_torrents;
        this.quality = quality;
        this.size = size;
        this.imdb_code = imdb_code;
        this.large_screenshot_image1 = large_screenshot_image1;
        this.large_screenshot_image2 = large_screenshot_image2;
        this.large_screenshot_image3 = large_screenshot_image3;
        this.url_cast_image = url_cast_image;
        this.cast_name = cast_name;
        this.char_name = char_name;
    }

    public Movies_Of_Year(String movie_id, String title, String rating, String large_cover_image) {
        this.movie_id = movie_id;
        this.title = title;
        this.rating = rating;
        this.large_cover_image = large_cover_image;
    }

    public String getMedium_cover_image() {
        return medium_cover_image;
    }

    public void setMedium_cover_image(String medium_cover_image) {
        this.medium_cover_image = medium_cover_image;
    }

    public String getCast_name() {
        return cast_name;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getYt_trailer_code() {
        return yt_trailer_code;
    }

    public void setYt_trailer_code(String yt_trailer_code) {
        this.yt_trailer_code = yt_trailer_code;
    }

    public String getLarge_cover_image() {
        return large_cover_image;
    }

    public void setLarge_cover_image(String large_cover_image) {
        this.large_cover_image = large_cover_image;
    }

    public String getUrl_torrents() {
        return url_torrents;
    }

    public void setUrl_torrents(String url_torrents) {
        this.url_torrents = url_torrents;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImdb_code() {
        return imdb_code;
    }

    public void setImdb_code(String imdb_code) {
        this.imdb_code = imdb_code;
    }

    public String getLarge_screenshot_image1() {
        return large_screenshot_image1;
    }

    public void setLarge_screenshot_image1(String large_screenshot_image1) {
        this.large_screenshot_image1 = large_screenshot_image1;
    }

    public String getLarge_screenshot_image2() {
        return large_screenshot_image2;
    }

    public void setLarge_screenshot_image2(String large_screenshot_image2) {
        this.large_screenshot_image2 = large_screenshot_image2;
    }

    public String getLarge_screenshot_image3() {
        return large_screenshot_image3;
    }

    public void setLarge_screenshot_image3(String large_screenshot_image3) {
        this.large_screenshot_image3 = large_screenshot_image3;
    }

    public String getUrl_cast_image() {
        return url_cast_image;
    }

    public void setUrl_cast_image(String url_cast_image) {
        this.url_cast_image = url_cast_image;
    }

    public void setCast_name(String cast_name) {
        this.cast_name = cast_name;
    }

    public String getChar_name() {
        return char_name;
    }

    public void setChar_name(String char_name) {
        this.char_name = char_name;
    }
}
