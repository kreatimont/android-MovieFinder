package io.kreatimont.cinematograph.model.tmdb.tv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Season extends RealmObject {

    @SerializedName("air_date")
    @Expose
    private String airDate;

    @SerializedName("episode_count")
    @Expose
    private Integer episodeCount;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("season_number")
    @Expose
    private Integer seasonNumber;

    /**
     * No args constructor for use in serialization
     *
     */
    public Season() {
    }

    /**
     *
     * @param id
     * @param airDate
     * @param posterPath
     * @param episodeCount
     * @param seasonNumber
     */
    public Season(String airDate, Integer episodeCount, Integer id, String posterPath, Integer seasonNumber) {
        super();
        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.id = id;
        this.posterPath = posterPath;
        this.seasonNumber = seasonNumber;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public Season withAirDate(String airDate) {
        this.airDate = airDate;
        return this;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Season withEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Season withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Season withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Season withSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
        return this;
    }

}