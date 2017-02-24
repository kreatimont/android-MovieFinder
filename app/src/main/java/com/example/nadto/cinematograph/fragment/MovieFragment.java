package com.example.nadto.cinematograph.fragment;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.nadto.cinematograph.Film;
import com.example.nadto.cinematograph.FilmAdapter;
import com.example.nadto.cinematograph.Json.JsonParser;
import com.example.nadto.cinematograph.MainActivity;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.RecyclerItemClickListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.example.nadto.cinematograph.MainActivity.ARRAY;

public class MovieFragment extends ListFragment {



    @Override
    void setUpData() {

        String TOP_MOVIES = "https://api.themoviedb.org/3/discover/movie?api_key=" + getActivity().getString(R.string.api_key) + "&sort_by=popularity.desc&include_adult=true";

        movies.clear();

        try {
            new ListFragment.GetMoviesTask(ARRAY, Film.MOVIE).execute(new URL(TOP_MOVIES));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mFilmAdapter = new FilmAdapter(getActivity(), movies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFilmAdapter);

    }

    @Override
    public void addItem(int id) {
        new GetMoviesTask("obj",Film.MOVIE).execute(
                JsonParser.createURL(new Random().nextInt(44444),Film.MOVIE,getActivity()));
    }

}
