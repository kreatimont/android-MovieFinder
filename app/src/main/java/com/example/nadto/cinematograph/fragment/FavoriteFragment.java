package com.example.nadto.cinematograph.fragment;


import android.support.v7.widget.LinearLayoutManager;

import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.adapter.FilmAdapter;
import com.example.nadto.cinematograph.db.DataBaseHelper;
import com.example.nadto.cinematograph.model.Film;

import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteFragment extends ListFragment {

    @Override
    void setUpData() {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        ArrayList<Film> films = dataBaseHelper.getAllFilm();

        movies.clear();
        movies = new ArrayList<>();

        jsonHelper = new JsonHelper(getActivity(),this);

        for(Film film : films) {
            jsonHelper.loadJson(jsonHelper.createURL(film.getId(),film.getType()));
        }

    }

    @Override
    public void setData(JSONObject jsonObject) {

        movies.add(jsonHelper.convertJsonToFilm(jsonObject));

        if(mFilmAdapter == null) {
            mFilmAdapter = new FilmAdapter(getActivity(), movies);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mFilmAdapter);
        }

        mFilmAdapter.notifyDataSetChanged();
    }
}
