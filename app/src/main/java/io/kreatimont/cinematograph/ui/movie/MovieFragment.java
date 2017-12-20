package io.kreatimont.cinematograph.ui.movie;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nadto.cinematograph.R;

import io.kreatimont.cinematograph.ui.main.MainActivity;
import io.kreatimont.cinematograph.ui.proto.fragment.ProtoFragment;
import io.kreatimont.cinematograph.helpers.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.helpers.RecyclerItemClickListener;
import io.kreatimont.cinematograph.data.ApiClient;
import io.kreatimont.cinematograph.data.api.ApiInterface;
import io.kreatimont.cinematograph.data.model.response.MoviesResponse;
import io.kreatimont.cinematograph.data.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieFragment extends ProtoFragment {

    private static boolean isLocalNetwork = true;

    @Override
    public void handleSearchQuery(String query) {

        if (isLocalNetwork) {
            ApiInterface localClient = ApiClient.getClientFor("http://10.0.2.2:8080/").create(ApiInterface.class);
            Call<List<Movie>> localCall = localClient.getMoviesBySearchCW(query);
            localCall.enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    Log.e("Local search response:", response.toString());

                    if (response.body() != null) {
                        mDataList.clear();
                        mDataList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();

                        checkEmptyState();
                    }

                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Log.e("Retrofit(failure)", t.getMessage());
                    checkEmptyState();
                }
            });
        } else {
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
    }

    @Override
    public void loadDataList(int page, ListType listType) {

        Call<MoviesResponse> call;

        switch (listType) {
            case Popular:
                call = apiService.getPopularMovies(getString(R.string.api_key), "ru", page);
                break;
            case TopRated:
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

                if(getActivity() != null) {
                    ((MainActivity)getActivity()).replaceFormWithProgressBar(false);
                }
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
    public void setUpUi() {
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
        mAdapter = new MovieAdapter(getActivity(), mDataList);
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
    public void retrieveFromDb() {
        this.mDataList.addAll(mRealm.where(Movie.class).findAll());
        mAdapter.notifyDataSetChanged();
    }

}
