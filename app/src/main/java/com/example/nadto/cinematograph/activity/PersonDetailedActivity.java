package com.example.nadto.cinematograph.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.CardLayoutType;
import com.example.nadto.cinematograph.adapter.EndlessRecyclerViewScrollListener;
import com.example.nadto.cinematograph.adapter.MovieAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.adapter.ResponseRecyclerViewAdapter;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.response.MoviesResponse;
import com.example.nadto.cinematograph.model.tmdb_model.movie.Movie;
import com.example.nadto.cinematograph.model.tmdb_model.people.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "id";

    private TextView name, biography, gender, birthday, placeOfBirth, link;
    private ImageView profilePhoto;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<Movie> mDataList;
    private MovieAdapter mAdapter;

    /*Activity lifecycle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_person);

        if(getIntent() != null ) {
            if(getIntent().getExtras() != null) {
                int personId = getIntent().getExtras().getInt(EXTRA_PERSON_ID);
                initUI();
                loadData(personId);
            }
        } else {
            this.onBackPressed();
        }

        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Configure UI*/

    private void initUI() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.personMoviesRecycler);
        name = (TextView) findViewById(R.id.detailedTitle);
        gender = (TextView) findViewById(R.id.detailedGender);
        biography = (TextView) findViewById(R.id.detProfileBiography);
        birthday = (TextView) findViewById(R.id.detProfileBirthday);
        placeOfBirth = (TextView) findViewById(R.id.detProfilePlaceOfBirth);
        link = (TextView) findViewById(R.id.detProfileLink);
        profilePhoto = (ImageView) findViewById(R.id.detailedBackdrop);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(PersonDetailedActivity.this, MovieDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(MovieDetailedActivity.EXTRA_ID, ((Movie)mDataList.get(position)).getId());
                            startActivity(intent);
                        } else {
                            Snackbar.make(collapsingToolbarLayout, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                }));

        mDataList = new ArrayList<>();
        mAdapter = new MovieAdapter(this, mDataList);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        ((ResponseRecyclerViewAdapter) mAdapter).setLayout(CardLayoutType.Grid);
    }

    /*Content providers*/

    private void loadData(int personId) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<Person> call = apiService.getPersonDetails(personId, getString(R.string.api_key), "ru");

        call.enqueue(new retrofit2.Callback<Person>() {

            @Override
            public void onResponse(retrofit2.Call<Person> call, retrofit2.Response<Person> response) {
                if(response.body() != null) {
                    Person responsePerson = response.body();
                    updateInfo(responsePerson);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());

            }

        });
    }

    private void loadPersonMovies(int personId) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<MoviesResponse> call = apiService.getDiscoverMovies(getString(R.string.api_key), "ru", String.valueOf(personId), null, null);

        call.enqueue(new retrofit2.Callback<MoviesResponse>() {

            @Override
            public void onResponse(retrofit2.Call<MoviesResponse> call, retrofit2.Response<MoviesResponse> response) {
                if(response.body() != null) {
                    List<Movie> responseMovies = response.body().getResults();
                    mDataList.addAll(responseMovies);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());

            }

        });
    }

    /*Additional methods*/

    private void updateInfo(Person person) {
        if(person != null) {


            collapsingToolbarLayout.setExpandedTitleMarginBottom(-999);
            collapsingToolbarLayout.setTitle(person.getName());

            name.setText(person.getName());
            biography.setText(person.getBiography());
            gender.setText(person.getGender() == 0 ? "male" : "female");
            birthday.setText(person.getBirthday());
            placeOfBirth.setText(person.getPlaceOfBirth());
            link.setText(person.getHomepage());
            Picasso.with(this).load(getString(R.string.image_base) + getString(R.string.profile_size_medium) + person.getProfilePath()).into(profilePhoto);

            loadPersonMovies(person.getId());
        }
    }

    public void onLinkClick(View view) {
        String url = ((TextView)view).getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
