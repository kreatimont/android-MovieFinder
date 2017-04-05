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
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.activity.MovieDetailedActivity;
import com.example.nadto.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import com.example.nadto.cinematograph.adapter.MovieAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.response.MoviesResponse;
import com.example.nadto.cinematograph.model.tmdb_model.movie.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private View rootView;
    private TextView emptyStub;
    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private ArrayList<Movie> movieArrayList;

    ApiInterface apiService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.movieRecycler);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MovieDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(MovieDetailedActivity.EXTRA_ID, movieArrayList.get(position).getId());
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

        movieArrayList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(getContext(), movieArrayList);

        mRecyclerView.setAdapter(mMovieAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.e("onLoadMore", "Page: " + page+1);
                loadMovieList(page + 1);

            }

        });

        apiService = ApiClient.getClient().create(ApiInterface.class);

        checkEmptyState();

        loadMovieList(1);

        return rootView;
    }

    void loadMovieList(int page) {



        Call<MoviesResponse> call = apiService.getTopRatedMovies(getString(R.string.api_key), "ru", page);

        call.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> responseMovies = response.body().getResults();

                movieArrayList.addAll(responseMovies);
                mMovieAdapter.notifyDataSetChanged();

                checkEmptyState();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
                checkEmptyState();
            }

        });

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

}
