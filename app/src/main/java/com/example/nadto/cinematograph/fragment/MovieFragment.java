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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.activity.MovieDetailedActivity;
import com.example.nadto.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import com.example.nadto.cinematograph.adapter.MovieAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.response.MoviesResponse;
import com.example.nadto.cinematograph.model.tmdb_model.movie.Movie;
import com.example.nadto.cinematograph.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment implements ProtoFragment {

    private View rootView;
    private TextView emptyStub;
    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieArrayList;

    ApiInterface apiService;

    private Realm mRealm;

    /*Fragment lifecycle*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(config);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        setHasOptionsMenu(true);

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

        if(InternetConnection.isConnected(getActivity())) {
            loadMovieList(1);
        } else {
            retrieveFromDb();
        }

        checkEmptyState();

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        Log.e("MovieFragment", "Option selected");

        if(id == R.id.grid2) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        if(id == R.id.grid3) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        if(id == R.id.grid4) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 4));
        }

        if(id == R.id.linear) {
            resetRecyclerViewLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    /*Data loader*/

    private void loadMovieList(int page) {

        Call<MoviesResponse> call = apiService.getTopRatedMovies(getString(R.string.api_key), "ru", page);

        call.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> responseMovies = response.body().getResults();

                movieArrayList.addAll(responseMovies);
                mMovieAdapter.notifyDataSetChanged();

                if(mRealm.where(Movie.class).findAll().size() > 100) {
                    mRealm.beginTransaction();
                    mRealm.deleteAll();
                    mRealm.commitTransaction();
                }

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(responseMovies);
                mRealm.commitTransaction();

                checkEmptyState();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
                checkEmptyState();
            }

        });

    }

    /*Additional*/

    private void checkEmptyState() {
        if(mMovieAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyStub.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyStub.setVisibility(View.VISIBLE);
        }
    }

    private void retrieveFromDb() {
        this.movieArrayList.addAll(mRealm.where(Movie.class).findAll());
        mMovieAdapter.notifyDataSetChanged();
    }

    private void resetRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
        if(layoutManager instanceof GridLayoutManager) {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadMovieList(page + 1);

                }

            });
        } else {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadMovieList(page + 1);

                }

            });
        }
        mMovieAdapter = new MovieAdapter(getActivity(), movieArrayList);
        mMovieAdapter.setGridLayout(true);
        mRecyclerView.setAdapter(mMovieAdapter);

    }

    /*Proto implementation*/

    @Override
    public void handleSearchQuery(String query) {
        Call<MoviesResponse> call = apiService.getMoviesByQuery(getString(R.string.api_key), "ru", query);

        call.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.body() == null) return;
                List<Movie> responseMovies = response.body().getResults();

                movieArrayList.clear();
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
}
