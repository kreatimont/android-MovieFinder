package com.example.nadto.cinematograph.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nadto.cinematograph.adapter.FilmAdapter;
import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.activity.FilmDetailedActivity;
import com.example.nadto.cinematograph.model.Film;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class ListFragment extends Fragment implements Client {

    public static final int LAYOUT_GRID2 = 666;
    public static final int LAYOUT_GRID3 = 777;
    public static final int LAYOUT_GRID4 = 888;
    public static final int LAYOUT_LINEAR = 999;

    RecyclerView mRecyclerView;
    FilmAdapter mFilmAdapter;
    ArrayList<Film> movies;
    View rootView;
    JsonHelper jsonHelper;
    String query;
    private RecyclerView.LayoutManager currentLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.movieRecycler);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent  intent = new Intent(getActivity(), FilmDetailedActivity.class);
                if(position >= 0) {
                    intent.putExtra(FilmDetailedActivity.EXTRA_ID, movies.get(position).getId());
                    intent.putExtra(FilmDetailedActivity.EXTRA_TYPE, movies.get(position).getType());
                    startActivity(intent);
                } else {
                    Snackbar.make(rootView, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));

        setUpData();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }

    public void loadItemsListFromUrl(URL url) {

    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if(layoutManager instanceof  GridLayoutManager) {
            mFilmAdapter.setGridLayout(true);
        } else {
            mFilmAdapter.setGridLayout(false);
        }
        currentLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(layoutManager);
        mFilmAdapter.setFilms(movies);
        mRecyclerView.setAdapter(mFilmAdapter);
        mFilmAdapter.notifyDataSetChanged();
    }

    public void setQuery(String type) {
        this.query = type;
    }

    public void setUpData() {

        movies = new ArrayList<>();

        jsonHelper = new JsonHelper(getActivity(),this);

        try {
            jsonHelper.loadJson(new URL(query));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setData(JSONObject jsonObject) {
        ArrayList<Film> newFilms = jsonHelper.convertJsonToFilmList(jsonObject);
        movies = new ArrayList<>();
        movies.addAll(newFilms);

        if(mFilmAdapter == null) {
            mFilmAdapter = new FilmAdapter(getContext(), movies);
        }

        setLayoutManager(currentLayoutManager != null ? currentLayoutManager : new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mFilmAdapter);
        mFilmAdapter.notifyDataSetChanged();
    }
}