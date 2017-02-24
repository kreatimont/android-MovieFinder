package com.example.nadto.cinematograph.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.example.nadto.cinematograph.Film;
import com.example.nadto.cinematograph.FilmAdapter;
import com.example.nadto.cinematograph.Json.JsonParser;
import com.example.nadto.cinematograph.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import static com.example.nadto.cinematograph.MainActivity.ARRAY;

public class SeriesFragment extends ListFragment {

    @Override
    void setUpData() {

        String TOP_SERIES = "https://api.themoviedb.org/3/discover/tv?api_key=" + getActivity().getString(R.string.api_key) + "&sort_by=popularity.desc&include_adult=true";

        movies.clear();

        try {
            new GetMoviesTask(ARRAY, Film.TV).execute(new URL(TOP_SERIES));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mFilmAdapter = new FilmAdapter(getActivity(), movies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFilmAdapter);

    }

    @Override
    public void addItem(int id) {
        new GetMoviesTask("obj", Film.TV).execute(
                JsonParser.createURL(new Random().nextInt(44444),Film.TV,getActivity()));
    }

}
