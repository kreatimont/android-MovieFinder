package io.kreatimont.cinematograph.fragment;

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

import io.kreatimont.cinematograph.adapter.CardLayoutType;
import io.kreatimont.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.adapter.ResponseRecyclerViewAdapter;
import io.kreatimont.cinematograph.api.ApiInterface;

import java.util.ArrayList;
import java.util.Random;

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

        CardLayoutType cardLayoutType;


        if(layoutManager instanceof GridLayoutManager) {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadDataList(page + 1, currentListType);

                }

            });
            cardLayoutType = CardLayoutType.Grid;

//            switch (((GridLayoutManager)layoutManager).getSpanCount()) {
//                case 2: break;
//                case 3: break;
//                case 4: break;
//                case 5: break;
//
//            }

        } else {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    Log.e("onLoadMore", "Page: " + page+1);
                    loadDataList(page + 1, currentListType);

                }

            });
            if(new Random().nextInt(2) % 2 == 0) {
                cardLayoutType = CardLayoutType.LinearWithBackdrop;
            } else {
                cardLayoutType = CardLayoutType.LinearWithPoster;
            }
        }

        ((ResponseRecyclerViewAdapter)mAdapter).setLayout(cardLayoutType);
        mRecyclerView.setAdapter(mAdapter);
    }

    void setListType(ListType listType) {
        currentListType = listType;
    }

    void startActivityNoHistory(Activity currentActivity, Class<? extends Activity> newTopActivityClass) {
        Intent intent = new Intent(currentActivity, newTopActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(intent);
    }


}
