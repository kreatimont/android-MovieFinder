package com.example.nadto.cinematograph.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import com.example.nadto.cinematograph.adapter.MovieAdapter;
import com.example.nadto.cinematograph.adapter.ResponseRecyclerViewAdapter;
import com.example.nadto.cinematograph.api.ApiInterface;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public  abstract class ProtoFragment extends Fragment implements InterfaceFragment {

    enum ListType {
        TOP_RATED,
        POPULAR
    }

    View rootView;
    TextView emptyStub;
    RecyclerView mRecyclerView;

    RecyclerView.Adapter mAdapter;
    ArrayList mDataList;

    ApiInterface apiService;

    Realm mRealm;

    ListType currentListType;

    /*Fragment lifecycle*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentListType = ListType.POPULAR;

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(config);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        emptyStub = (TextView) rootView.findViewById(R.id.emptyStub);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.movieRecycler);

        setHasOptionsMenu(true);

        setUpUi();

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.grid2) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 2));
            return true;
        }

        if(id == R.id.grid3) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 3));
            return true;
        }

        if(id == R.id.grid4) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 4));
            return true;
        }

        if(id == R.id.grid5) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 5));
            return true;
        }

        if(id == R.id.linear) {
            resetRecyclerViewLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    /*Unique methods*/

    abstract void loadDataList(int page, ListType listType);

    abstract void setUpUi();

    abstract void retrieveFromDb();

    /*Additional*/

    void checkEmptyState() {
        if(mAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyStub.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyStub.setVisibility(View.VISIBLE);
        }
    }

    void resetRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);

        boolean withGridLayout;

        if(layoutManager instanceof GridLayoutManager) {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadDataList(page + 1, currentListType);

                }

            });
            withGridLayout = true;
        } else {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadDataList(page + 1, currentListType);

                }

            });
            withGridLayout = false;
        }
        resetAdapter(withGridLayout);
    }

    void resetAdapter(boolean withGridLayout) {
        ((ResponseRecyclerViewAdapter) mAdapter).setGridLayout(withGridLayout);
        mRecyclerView.setAdapter(mAdapter);
    }

    void setListType(ListType listType) {
        currentListType = listType;
    }


}
