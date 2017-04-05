package com.example.nadto.cinematograph.model.tmdb_model.credits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kreatimont on 4/5/17.
 */

public class Credits {

    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Credits() {
    }

    /**
     *
     * @param cast
     * @param crew
     */
    public Credits(List<Cast> cast, List<Crew> crew) {
        super();
        this.cast = cast;
        this.crew = crew;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public Credits withCast(List<Cast> cast) {
        this.cast = cast;
        return this;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public Credits withCrew(List<Crew> crew) {
        this.crew = crew;
        return this;
    }

}
