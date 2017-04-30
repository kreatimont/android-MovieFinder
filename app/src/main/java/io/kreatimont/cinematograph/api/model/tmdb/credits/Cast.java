package io.kreatimont.cinematograph.api.model.tmdb.credits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Cast extends RealmObject {

    @PrimaryKey
    @SerializedName("cast_id")
    @Expose
    private Integer castId;

    @SerializedName("character")
    @Expose
    private String character;

    @SerializedName("credit_id")
    @Expose
    private String creditId;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("order")
    @Expose
    private Integer order;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    /**
     * No args constructor for use in serialization
     *
     */
    public Cast() {
    }

    /**
     *
     * @param id
     * @param profilePath
     * @param order
     * @param castId
     * @param name
     * @param creditId
     * @param character
     */
    public Cast(Integer castId, String character, String creditId, Integer id, String name, Integer order, String profilePath) {
        super();
        this.castId = castId;
        this.character = character;
        this.creditId = creditId;
        this.id = id;
        this.name = name;
        this.order = order;
        this.profilePath = profilePath;
    }

    public Integer getCastId() {
        return castId;
    }

    public void setCastId(Integer castId) {
        this.castId = castId;
    }

    public Cast withCastId(Integer castId) {
        this.castId = castId;
        return this;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Cast withCharacter(String character) {
        this.character = character;
        return this;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Cast withCreditId(String creditId) {
        this.creditId = creditId;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cast withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cast withName(String name) {
        this.name = name;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Cast withOrder(Integer order) {
        this.order = order;
        return this;
    }

    public Object getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Cast withProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }

}
