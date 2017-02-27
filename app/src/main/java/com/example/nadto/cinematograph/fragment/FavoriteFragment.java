package com.example.nadto.cinematograph.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;

import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.FilmAdapter;
import com.example.nadto.cinematograph.model.Film;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.nadto.cinematograph.activity.MainActivity.APP_PREFERENCE;

public class FavoriteFragment extends ListFragment {

    @Override
    void setUpData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                APP_PREFERENCE,Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt(getString(R.string.saved_id), 44217);
        int type = sharedPreferences.getInt(getString(R.string.saved_type), Film.TV);

        movies.clear();
        movies = new ArrayList<>();

        jsonHelper = new JsonHelper(getActivity(),this);

        jsonHelper.loadJson(jsonHelper.createURL(id,type));
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
