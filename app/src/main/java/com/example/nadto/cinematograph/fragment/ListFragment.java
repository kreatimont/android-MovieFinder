package com.example.nadto.cinematograph.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.example.nadto.cinematograph.activity.DetailedActivity;
import com.example.nadto.cinematograph.model.Film;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public abstract class ListFragment extends Fragment implements Client {

    RecyclerView mRecyclerView;
    FilmAdapter mFilmAdapter;
    ArrayList<Film> movies;
    View rootView;
    JsonHelper jsonHelper;
    String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.movieRecycler);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent  intent = new Intent(getActivity(), DetailedActivity.class);
                if(position >= 0) {
                    intent.putExtra(DetailedActivity.EXTRA_ID, movies.get(position).getId());
                    intent.putExtra(DetailedActivity.EXTRA_TYPE, movies.get(position).getType());
                    startActivity(intent);
                } else {
                    Snackbar.make(rootView, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        addFakeItems();

        setUpData();

        return rootView;
    }

    void addFakeItems() {
        movies = new ArrayList<>();
        movies.add(new Film(
                44217,
                Film.TV,
                Film.STATUS_RETURNING,
                "Vikings","2011",
                "Micheal Hirst",
                7.8f, "Drama",
                "null",
                "null",
                "Canada",
                "Vikings journey.",
                "Northmans",
                7f,
                130000));
    }

    void setFilmType(String type) {
        this.type = type;
    }

    void setUpData() {

        jsonHelper = new JsonHelper(getActivity(),this);

        try {
            jsonHelper.loadJson(new URL(type));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setData(JSONObject jsonObject) {
        movies.clear();
        movies = jsonHelper.convertJsonToFilmList(jsonObject);

        if(mFilmAdapter == null) {
            mFilmAdapter = new FilmAdapter(getActivity(), movies);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mFilmAdapter);
        }
        
        Log.e("TAG","Before notify");
        Log.e("TAG", "Array size:" + movies.size());
        mFilmAdapter.notifyDataSetChanged();
    }
}
