package com.example.nadto.cinematograph.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.nadto.cinematograph.activity.MovieDetailedActivity;
import com.example.nadto.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import com.example.nadto.cinematograph.adapter.MovieAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.adapter.TvAdapter;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.response.MoviesResponse;
import com.example.nadto.cinematograph.model.response.TvResponse;
import com.example.nadto.cinematograph.model.tmdb_model.movie.Movie;
import com.example.nadto.cinematograph.model.tmdb_model.tv.Tv;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {

    private View rootView;
    private TextView emptyStub;
    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private MovieAdapter mMovieAdapter;
    private TvAdapter mTvAdapter;

    private ArrayList<Movie> movies;
    private ArrayList<Tv> tvArrayList;

    private boolean isMovie = true;

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

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.movieRecycler);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent  intent = new Intent(getActivity(), MovieDetailedActivity.class);
                if(position >= 0) {
                    intent.putExtra(MovieDetailedActivity.EXTRA_ID, movies.get(position).getId());
                    //intent.putExtra(MovieDetailedActivity.EXTRA_TYPE, Film.MOVIE);
                    startActivity(intent);
                } else {
                    Snackbar.make(rootView, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));

        emptyStub = (TextView) rootView.findViewById(R.id.emptyStub);

        movies = new ArrayList<>();
        tvArrayList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(getContext(), movies);
        mRecyclerView.setAdapter(mMovieAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.e("onLoadMore", "Page: " + page+1);
                retrofitLoadMoviesList(page + 1);

//                clearBeforeAdd = false;
//                Log.e("onLoadMore", "Page:" + page);
//                String query =  getString(R.string.base_url_movie)
//                        + getString(R.string.mode_popular)
//                        + getString(R.string.api_key_prefix)
//                        + getString(R.string.api_key)
//                        + getString(R.string.mode_page, page);
//                ApiManager.getInstance().loadFilmList(query, ListFragment.this, getActivity());
            }

        });

        return rootView;
    }

    /*Content providers*/

    /*Api listener implementation*/

    public void retrofitLoadMoviesList(int page) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(getString(R.string.api_key), "ru", page);

        call.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> responseMovies = response.body().getResults();

                movies.addAll(responseMovies);
                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
            }

        });

    }

    public void retrofitLoadTvList(int page) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTopRatedTv(getString(R.string.api_key), "ru", page);

        call.enqueue(new Callback<TvResponse>() {

            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                List<Tv> responseTv = response.body().getResults();

                tvArrayList.addAll(responseTv);
                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
            }

        });

    }

    /*Additional methods*/

    public void setUpTvAdapter() {
        retrofitLoadTvList(1);
        if(mTvAdapter == null) {
            mTvAdapter = new TvAdapter(getContext(), tvArrayList);
        }
        mRecyclerView.setAdapter(mTvAdapter);
        mTvAdapter.notifyDataSetChanged();
    }

    public void checkEmptyState() {
        if(mMovieAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyStub.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyStub.setVisibility(View.VISIBLE);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if(layoutManager instanceof  GridLayoutManager) {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    retrofitLoadMoviesList(page + 1);
                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
            mMovieAdapter.setGridLayout(true);
        } else {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    retrofitLoadMoviesList(page + 1);
                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
            mMovieAdapter.setGridLayout(false);
        }

        //mMovieAdapter.setFilms(movies);

        mRecyclerView.setAdapter(mMovieAdapter);
        //mMovieAdapter.notifyDataSetChanged();
    }
}