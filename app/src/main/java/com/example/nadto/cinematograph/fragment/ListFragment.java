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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.activity.FilmDetailedActivity;
import com.example.nadto.cinematograph.activity.MainActivity;
import com.example.nadto.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import com.example.nadto.cinematograph.adapter.FilmAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.api.ApiListener;
import com.example.nadto.cinematograph.api.ApiManager;
import com.example.nadto.cinematograph.api.JsonHelper;
import com.example.nadto.cinematograph.db.DataBaseHelper;
import com.example.nadto.cinematograph.model.Film;

import java.util.ArrayList;

public class ListFragment extends Fragment implements ApiListener {

    private View rootView;
    private TextView emptyStub;
    private RecyclerView mRecyclerView;

    private FilmAdapter mFilmAdapter;
    private ArrayList<Film> movies;

    private JsonHelper jsonHelper;

    private boolean clearBeforeAdd = false;

    /*Fragment lifecycle*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R   .id.movieRecycler);
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

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(dy > 0) {
//                    ((FloatingActionButton)getActivity().findViewById(R.id.fabPrev)).hide();
//                    ((FloatingActionButton)getActivity().findViewById(R.id.fabNext)).hide();
//                } else if(dy < 0) {
//                    if(getActivity().findViewById(R.id.fabPrev).getVisibility() != View.INVISIBLE
//                            && !((MainActivity)getActivity()).getCurrentCategory().equals(MainActivity.FAVORITES)) {
//                        ((FloatingActionButton)getActivity().findViewById(R.id.fabPrev)).show();
//                    }
//                    if(!((MainActivity)getActivity()).getCurrentCategory().equals(MainActivity.FAVORITES)) {
//                        ((FloatingActionButton)getActivity().findViewById(R.id.fabNext)).show();
//                    }
//                }
//            }
//        });

        emptyStub = (TextView) rootView.findViewById(R.id.emptyStub);

        movies = new ArrayList<>();

        mFilmAdapter = new FilmAdapter(getContext(), movies);

        mRecyclerView.setAdapter(mFilmAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                clearBeforeAdd = false;
                String query =  getString(R.string.base_url_movie)
                        + getString(R.string.mode_popular)
                        + getString(R.string.api_key_prefix)
                        + getString(R.string.api_key)
                        + getString(R.string.mode_page, page);
                ApiManager.getInstance().loadFilmList(query, ListFragment.this, getActivity());
            }
        });



        jsonHelper = new JsonHelper(getContext());

        return rootView;
    }

    /*Content providers*/

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

    public void loadItemListFromUrl(String url, final boolean clearBeforeAdd) {
        this.clearBeforeAdd = clearBeforeAdd;
        ApiManager.getInstance().loadFilmList(url, this, getActivity());
    }

    public void loadItemFromUrl(String url, final boolean clearBeforeAdd) {
        this.clearBeforeAdd = clearBeforeAdd;
        ApiManager.getInstance().loadFilm(url, this, getActivity());
    }

    /*Api listener implementation*/

    @Override
    public void successfullyLoadFilm(Film film) {
        Log.e("Impl methods","success item");
        if(clearBeforeAdd) {
            movies.clear();
        }
        movies.add(film);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFilmAdapter.notifyDataSetChanged();
            }
        });
        checkEmptyState();
    }

    @Override
    public void successfullyLoadFilmList(ArrayList<Film> films) {
        Log.e("Impl methods","success list");
        if(clearBeforeAdd) {
            movies.clear();
        }
        movies.addAll(films);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFilmAdapter.notifyDataSetChanged();
            }
        });
        checkEmptyState();
    }

    @Override
    public void connectionError() {
        checkEmptyState();
    }

    @Override
    public void parseError() {
        checkEmptyState();
    }

    /*Additional methods*/

    public void checkEmptyState() {
        if(mFilmAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyStub.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyStub.setVisibility(View.VISIBLE);
        }
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