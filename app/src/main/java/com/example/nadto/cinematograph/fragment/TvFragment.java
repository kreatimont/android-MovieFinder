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
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.adapter.TvAdapter;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.response.TvResponse;
import com.example.nadto.cinematograph.model.tmdb_model.tv.Tv;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kreatimont on 4/5/17.
 */

public class TvFragment extends Fragment {

    private View rootView;
    private TextView emptyStub;
    private RecyclerView mRecyclerView;

    private TvAdapter mTvAdapter;

    private ArrayList<Tv> tvArrayList;

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
                            intent.putExtra(MovieDetailedActivity.EXTRA_ID, tvArrayList.get(position).getId());
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

        tvArrayList = new ArrayList<>();

        mTvAdapter = new TvAdapter(getContext(), tvArrayList);

        mRecyclerView.setAdapter(mTvAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.e("onLoadMore", "Page: " + page+1);
                loadTvList(page + 1);

            }

        });

        apiService = ApiClient.getClient().create(ApiInterface.class);

        checkEmptyState();

        loadTvList(1);

        return rootView;
    }

    void loadTvList(int page) {



        Call<TvResponse> call = apiService.getTopRatedTv(getString(R.string.api_key), "ru", page);

        call.enqueue(new Callback<TvResponse>() {

            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                List<Tv> responseTv = response.body().getResults();

                tvArrayList.addAll(responseTv);
                mTvAdapter.notifyDataSetChanged();

                checkEmptyState();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
                checkEmptyState();
            }

        });

    }

    public void checkEmptyState() {
        if(mTvAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyStub.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyStub.setVisibility(View.VISIBLE);
        }
    }
}
