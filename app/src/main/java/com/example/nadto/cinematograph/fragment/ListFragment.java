package com.example.nadto.cinematograph.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.activity.FilmDetailedActivity;
import com.example.nadto.cinematograph.activity.MainActivity;
import com.example.nadto.cinematograph.adapter.FilmAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.db.DataBaseHelper;
import com.example.nadto.cinematograph.model.Film;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListFragment extends Fragment {

    RecyclerView mRecyclerView;

    FilmAdapter mFilmAdapter;

    ArrayList<Film> movies;

    View rootView;

    JsonHelper jsonHelper;

    private OkHttpClient httpClient;


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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    ((FloatingActionButton)getActivity().findViewById(R.id.fabPrev)).hide();
                    ((FloatingActionButton)getActivity().findViewById(R.id.fabNext)).hide();
                } else if(dy < 0) {
                    if(getActivity().findViewById(R.id.fabPrev).getVisibility() != View.INVISIBLE
                            && !((MainActivity)getActivity()).getCurrentCategory().equals(MainActivity.FAVORITES)) {
                        ((FloatingActionButton)getActivity().findViewById(R.id.fabPrev)).show();
                    }
                    if(!((MainActivity)getActivity()).getCurrentCategory().equals(MainActivity.FAVORITES)) {
                        ((FloatingActionButton)getActivity().findViewById(R.id.fabNext)).show();
                    }
                }
            }
        });
        movies = new ArrayList<>();

        mFilmAdapter = new FilmAdapter(getContext(), movies);
        mRecyclerView.setAdapter(mFilmAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        httpClient = new OkHttpClient();

        jsonHelper = new JsonHelper(getContext());

//        addTestItems();

//        setUpData();

        return rootView;
    }

    public void loadItemsFromDb(boolean clearBeforeAdd) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        ArrayList<Film> films = dataBaseHelper.getAllFilm();

        if(movies == null) {
            movies = new ArrayList<>();
        }

        if(clearBeforeAdd) {
            movies.clear();
        }

        jsonHelper = new JsonHelper(getActivity());

        for(Film film : films) {
            loadItemFromUrl(jsonHelper.createURL(film.getId(), film.getType()).toString(), false);
        }
        mFilmAdapter.notifyDataSetChanged();
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

    public void loadItemListFromUrl(String url, final boolean clearBeforeAdd) {
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String responseData = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(clearBeforeAdd) {
                                    movies.clear();
                                }
                                movies.addAll(jsonHelper.convertJsonToFilmList(new JSONObject(responseData)));
                                mFilmAdapter.notifyDataSetChanged();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public void loadItemFromUrl(String url, final boolean clearBeforeAdd) {
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String responseData = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(clearBeforeAdd) {
                                    movies.clear();
                                }
                                movies.add(jsonHelper.convertJsonToFilm(new JSONObject(responseData)));
                                mFilmAdapter.notifyItemInserted(movies.size() - 1);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if(layoutManager instanceof  GridLayoutManager) {
            mFilmAdapter.setGridLayout(true);
        } else {
            mFilmAdapter.setGridLayout(false);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mFilmAdapter.setFilms(movies);

        mRecyclerView.setAdapter(mFilmAdapter);
        mFilmAdapter.notifyDataSetChanged();
    }

}