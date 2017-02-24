package com.example.nadto.cinematograph;


public class Film {

    public static final int MOVIE = 1882;
    public static final int TV = 1952;

    private int id;
    private int type;
    private  String title, date, createdBy;
    private float voteAverage;
    private String genres;
    private String countries;
    private String pathToPoster, pathToBackdrop;

    public Film(int id, int type, String title, String date, String createdBy , float voteAverage, String genres, String pathToPoster, String pathToBackdrop, String countries) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.date = date;
        this.voteAverage = voteAverage;
        this.createdBy = createdBy;
        this.pathToPoster = pathToPoster;
        this.pathToBackdrop = pathToBackdrop;
        this.genres = genres;
        this.countries = countries;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathToPoster() {
        return pathToPoster;
    }

    public void setPathToPoster(String pathToPoster) {
        this.pathToPoster = pathToPoster;
    }

    public String getPathToBackdrop() {
        return pathToBackdrop;
    }

    public void setPathToBackdrop(String pathToBackdrop) {
        this.pathToBackdrop = pathToBackdrop;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
