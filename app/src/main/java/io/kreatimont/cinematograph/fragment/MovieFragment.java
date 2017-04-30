package io.kreatimont.cinematograph.fragment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nadto.cinematograph.R;
import io.kreatimont.cinematograph.ui.MainActivity;
import io.kreatimont.cinematograph.ui.MovieDetailedActivity;
import io.kreatimont.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.adapter.MovieAdapter;
import io.kreatimont.cinematograph.adapter.RecyclerItemClickListener;
import io.kreatimont.cinematograph.api.ApiClient;
import io.kreatimont.cinematograph.api.ApiInterface;
import io.kreatimont.cinematograph.model.response.MoviesResponse;
import io.kreatimont.cinematograph.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends ProtoFragment {

    @Override
    public void handleSearchQuery(String query) {
        Call<MoviesResponse> call = apiService.getMoviesByQuery(getString(R.string.api_key), "ru", query);

        call.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.body() == null) return;
                List<Movie> responseMovies = response.body().getResults();

                mDataList.clear();
                mDataList.addAll(responseMovies);
                mAdapter.notifyDataSetChanged();

                checkEmptyState();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
                checkEmptyState();
            }

        });
    }

    @Override
    void loadDataList(int page, ListType listType) {

        Call<MoviesResponse> call;

        switch (listType) {
            case POPULAR:
                call = apiService.getPopularMovies(getString(R.string.api_key), "ru", page);
                break;
            case TOP_RATED:
                call = apiService.getTopRatedMovies(getString(R.string.api_key), "ru", page);
                break;
            default:
                call = apiService.getPopularMovies(getString(R.string.api_key), "ru", page);
                break;
        }

        call.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> responseMovies = response.body().getResults();

                mDataList.addAll(responseMovies);
                mAdapter.notifyDataSetChanged();

                if(mRealm.where(Movie.class).findAll().size() > 100) {
                    mRealm.beginTransaction();
                    mRealm.deleteAll();
                    mRealm.commitTransaction();
                }

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(responseMovies);
                mRealm.commitTransaction();

                checkEmptyState();
                ((MainActivity)getActivity()).replaceFormWithProgressBar(false);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
                Log.e("URL:", call.request().toString());
                checkEmptyState();
                ((MainActivity)getActivity()).replaceFormWithProgressBar(false);
            }

        });
    }

    @Override
    void setUpUi() {
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MovieDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(MovieDetailedActivity.EXTRA_ID, ((Movie)mDataList.get(position)).getId());

                            /*clear activity stack*/
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        } else {
                            Snackbar.make(rootView, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                }));

        mDataList = new ArrayList<>();
        mAdapter = new MovieAdapter(getContext(), mDataList);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadDataList(page + 1, currentListType);
            }

        });

        apiService = ApiClient.getClient().create(ApiInterface.class);

        if(InternetConnection.isConnected(getActivity())) {
            loadDataList(1, currentListType);
        } else {
            retrieveFromDb();
        }

        checkEmptyState();
    }

    @Override
    void retrieveFromDb() {
        this.mDataList.addAll(mRealm.where(Movie.class).findAll());
        mAdapter.notifyDataSetChanged();
    }

}