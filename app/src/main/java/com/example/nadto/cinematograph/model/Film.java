package com.example.nadto.cinematograph.model;


public class Film {

    public static final int MOVIE = 1882;
    public static final int TV = 1952;
    public static final int PERSON = 1812;
    public static final String STATUS_RELEASED = "Released";
    public static final String STATUS_RETURNING = "Returning Series";

    private int id ,type, budget;
    private  String title, date, createdBy, genres, countries, pathToPoster, pathToBackdrop, overview, tagline, status;
    private float voteAverage, popularity;

    public Film(int id,
                int type,
                String status,
                String title,
                String date,
                String createdBy,
                float voteAverage,
                String genres,
                String pathToPoster,
                String pathToBackdrop,
                String countries,
                String overview,
                String tagline,
                float popularity,
                int budget) {
        this.type = type;
        this.id = id;
        this.status = status;
        this.title = title;
        this.date = date;
        this.voteAverage = voteAverage;
        this.createdBy = createdBy;
        this.pathToPoster = pathToPoster;
        this.pathToBackdrop = pathToBackdrop;
        this.genres = genres;
        this.countries = countries;
        this.overview = overview;
        this.tagline = tagline;
        this.popularity = popularity;
        this.budget = budget;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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
