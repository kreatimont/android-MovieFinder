package com.example.nadto.cinematograph.api;

import com.example.nadto.cinematograph.model.Film;

import java.util.ArrayList;

public interface ApiListener {

    void successfullyLoadFilm(Film film);
    void successfullyLoadFilmList(ArrayList<Film> films);
    void connectionError();
    void parseError();
}
