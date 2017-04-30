package io.kreatimont.cinematograph.fragment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nadto.cinematograph.R;

import io.kreatimont.cinematograph.ui.TvDetailedActivity;
import io.kreatimont.cinematograph.ui.MainActivity;
import io.kreatimont.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.adapter.RecyclerItemClickListener;
import io.kreatimont.cinematograph.adapter.TvAdapter;
import io.kreatimont.cinematograph.api.ApiClient;
import io.kreatimont.cinematograph.api.ApiInterface;
import io.kreatimont.cinematograph.model.response.TvResponse;
import io.kreatimont.cinematograph.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvFragment extends ProtoFragment {

    @Override
    public void handleSearchQuery(String query) {
        Call<TvResponse> call = apiService.getTvByQuery(getString(R.string.api_key), "ru", query);

        call.enqueue(new Callback<TvResponse>() {

            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                if(response.body() == null) return;
                List<Tv> responseTv = response.body().getResults();

                mDataList.clear();
                mDataList.addAll(responseTv);
                mAdapter.notifyDataSetChanged();

                checkEmptyState();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
                checkEmptyState();
            }

        });
    }

    @Override
    void loadDataList(int page, ListType listType) {

        Call<TvResponse> call;

        switch (listType) {
            case POPULAR:
                call = apiService.getPopularTv(getString(R.string.api_key), "ru", page);
                break;
            case TOP_RATED:
                call = apiService.getTopRatedTv(getString(R.string.api_key), "ru", page);
                break;
            default:
                call = apiService.getPopularTv(getString(R.string.api_key), "ru", page);
                break;
        }

        call.enqueue(new Callback<TvResponse>() {

            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                List<Tv> responseTv = response.body().getResults();

                mDataList.addAll(responseTv);
                mAdapter.notifyDataSetChanged();

                if(mRealm.where(Tv.class).findAll().size() > 100) {
                    mRealm.beginTransaction();
                    mRealm.deleteAll();
                    mRealm.commitTransaction();
                }

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(responseTv);
                mRealm.commitTransaction();

                checkEmptyState();

                ((MainActivity)getActivity()).replaceFormWithProgressBar(false);
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
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
                        Intent intent = new Intent(getActivity(), TvDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(TvDetailedActivity.EXTRA_ID, ((Tv)mDataList.get(position)).getId());

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
        mAdapter = new TvAdapter(getContext(), mDataList);
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
        this.mDataList.addAll(mRealm.where(Tv.class).findAll());
        mAdapter.notifyDataSetChanged();
    }

}