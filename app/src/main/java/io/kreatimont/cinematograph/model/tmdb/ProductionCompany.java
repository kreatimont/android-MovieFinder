package io.kreatimont.cinematograph.model.tmdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductionCompany extends RealmObject{

    @SerializedName("name")
    @Expose
    private String name;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProductionCompany() {
    }

    /**
     *
     * @param id
     * @param name
     */
    public ProductionCompany(String name, Integer id) {
        super();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductionCompany withName(String name) {
        this.name = name;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductionCompany withId(Integer id) {
        this.id = id;
        return this;
    }

}