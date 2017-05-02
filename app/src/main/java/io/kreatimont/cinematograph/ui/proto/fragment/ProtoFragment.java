package io.kreatimont.cinematograph.ui.proto.fragment;

import android.app.Activity;
import android.content.Intent;
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

import io.kreatimont.cinematograph.data.api.ApiInterface;

import java.util.ArrayList;

import io.kreatimont.cinematograph.helpers.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.ui.proto.adapter.MovieCardLayoutType;
import io.kreatimont.cinematograph.ui.proto.adapter.ResponseRecyclerViewAdapter;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public  abstract class ProtoFragment extends Fragment implements InterfaceFragment {

    public enum ListType {
        TopRated,
        Popular
    }

    public View rootView;
    public TextView emptyStub;
    public RecyclerView mRecyclerView;

    public RecyclerView.Adapter mAdapter;
    public ArrayList mDataList;

    public ApiInterface apiService;

    public Realm mRealm;

    public ListType currentListType;

    /*Fragment lifecycle*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentListType = ListType.Popular;

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
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 2), MovieCardLayoutType.Grid);
            return true;
        }

        if(id == R.id.grid3) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 3), MovieCardLayoutType.Grid);
            return true;
        }

        if(id == R.id.grid4) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 4), MovieCardLayoutType.Grid);
            return true;
        }

        if(id == R.id.grid5) {
            resetRecyclerViewLayoutManager(new GridLayoutManager(getActivity(), 5), MovieCardLayoutType.Grid);
            return true;
        }

        if(id == R.id.horizontal) {
            resetRecyclerViewLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false), MovieCardLayoutType.LinearHorizontal);
            return true;
        }

        if(id == R.id.linear) {
            resetRecyclerViewLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false), MovieCardLayoutType.LinearWithBackdrop);
            return true;
        }

        if(id == R.id.linearPoster) {
            resetRecyclerViewLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false), MovieCardLayoutType.LinearWithPoster);
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

    public abstract void loadDataList(int page, ListType listType);

    public abstract void setUpUi();

    public abstract void retrieveFromDb();

    /*Additional*/

    public void checkEmptyState() {
        if(mAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyStub.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyStub.setVisibility(View.VISIBLE);
        }
    }

    public void resetRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager, MovieCardLayoutType type) {
        mRecyclerView.setLayoutManager(layoutManager);

        if(layoutManager instanceof GridLayoutManager) {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadDataList(page + 1, currentListType);

                }

            });
        } else {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadDataList(page + 1, currentListType);

                }

            });
        }

        ((ResponseRecyclerViewAdapter)mAdapter).setLayout(type);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setListType(ListType listType) {
        currentListType = listType;
    }

    public void startActivityNoHistory(Activity currentActivity, Class<? extends Activity> newTopActivityClass) {
        Intent intent = new Intent(currentActivity, newTopActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(intent);
    }


}
