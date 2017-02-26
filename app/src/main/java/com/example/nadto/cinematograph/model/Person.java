package com.example.nadto.cinematograph.model;

public class Person {

    private int id;
    private String name, currentChar, profilePath, biography, birthday, deathday, placeOfBirth, gender, link;
    private double popularity;

    public Person(int id, String name, String currentChar, String profilePath) {
        this.id = id;
        this.name = name;
        this.currentChar = currentChar;
        this.profilePath = profilePath;
    }

    public Person(int id,
                  String name,
                  String currentChar,
                  String profilePath,
                  String biography,
                  String birthday,
                  String deathday,
                  String placeOfBirth,
                  String gender,
                  String link,
                  double popularity) {

        this.id = id;
        this.name = name;
        this.currentChar = currentChar;
        this.profilePath = profilePath;
        this.biography = biography;
        this.birthday = birthday;
        this.deathday = deathday;
        this.placeOfBirth = placeOfBirth;
        this.gender = gender;
        this.link = link;
        this.popularity = popularity;

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBiography() {
        return biography;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentChar() {
        return currentChar;
    }

    public void setCurrentChar(String currentChar) {
        this.currentChar = currentChar;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
