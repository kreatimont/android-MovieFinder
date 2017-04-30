package io.kreatimont.cinematograph.ui.person;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nadto.cinematograph.R;

import io.kreatimont.cinematograph.helpers.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.helpers.RecyclerItemClickListener;
import io.kreatimont.cinematograph.api.ApiClient;
import io.kreatimont.cinematograph.api.ApiInterface;
import io.kreatimont.cinematograph.api.model.response.PersonResponse;
import io.kreatimont.cinematograph.api.model.tmdb.people.Person;
import io.kreatimont.cinematograph.ui.main.MainActivity;
import io.kreatimont.cinematograph.ui.proto.fragment.ProtoFragment;
import io.kreatimont.cinematograph.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonFragment extends ProtoFragment {

    @Override
    public void handleSearchQuery(String query) {

        Call<PersonResponse> call = apiService.getPersonsByQuery(getString(R.string.api_key), "ru", query);

        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                if(response.body() == null) return;
                List<Person> responsePersons = response.body().getResults();

                mDataList.clear();
                mDataList.addAll(responsePersons);
                mAdapter.notifyDataSetChanged();

                checkEmptyState();
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void loadDataList(int page, ListType listType) {

        Call<PersonResponse> call;

        call = apiService.getPopularPersons(getString(R.string.api_key), "ru", page);

        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                List<Person> responsePersons = response.body().getResults();

                mDataList.addAll(responsePersons);
                mAdapter.notifyDataSetChanged();

                checkEmptyState();
                ((MainActivity)getActivity()).replaceFormWithProgressBar(false);
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
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
                        Intent intent = new Intent(getActivity(), PersonDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(PersonDetailedActivity.EXTRA_PERSON_ID, ((Person)mDataList.get(position)).getId());

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
        mAdapter = new PersonAdapter(getContext(), mDataList);
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

    }
}
