package io.kreatimont.cinematograph.model.tmdb.credits;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Crew extends RealmObject {

    @SerializedName("credit_id")
    @Expose
    private String creditId;

    @SerializedName("department")
    @Expose
    private String department;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("job")
    @Expose
    private String job;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    /**
     * No args constructor for use in serialization
     *
     */
    public Crew() {
    }

    /**
     *
     * @param id
     * @param profilePath
     * @param department
     * @param name
     * @param job
     * @param creditId
     */
    public Crew(String creditId, String department, Integer id, String job, String name, String profilePath) {
        super();
        this.creditId = creditId;
        this.department = department;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Crew withCreditId(String creditId) {
        this.creditId = creditId;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Crew withDepartment(String department) {
        this.department = department;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Crew withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Crew withJob(String job) {
        this.job = job;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Crew withName(String name) {
        this.name = name;
        return this;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Crew withProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }

}
