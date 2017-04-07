package com.example.nadto.cinematograph.model.tmdb_model.credits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Credits extends RealmObject {

    @SerializedName("cast")
    @Expose
    private RealmList<Cast> cast = null;

    @SerializedName("crew")
    @Expose
    private RealmList<Crew> crew = null;

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
    public Credits(RealmList<Cast> cast, RealmList<Crew> crew) {
        super();
        this.cast = cast;
        this.crew = crew;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(RealmList<Cast> cast) {
        this.cast = cast;
    }

    public Credits withCast(RealmList<Cast> cast) {
        this.cast = cast;
        return this;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(RealmList<Crew> crew) {
        this.crew = crew;
    }

    public Credits withCrew(RealmList<Crew> crew) {
        this.crew = crew;
        return this;
    }

}
