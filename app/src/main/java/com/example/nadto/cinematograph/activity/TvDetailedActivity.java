package com.example.nadto.cinematograph.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.ProfileAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.tmdb_model.credits.Cast;
import com.example.nadto.cinematograph.model.tmdb_model.tv.Tv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.nadto.cinematograph.activity.PersonDetailedActivity.EXTRA_PERSON_ID;

public class TvDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private ImageView backdrop, poster;
    private TextView overview, year, createdBy, budget, genres, popularity, vote, tagline;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProfileAdapter profileAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fabFavorite;


    /*Activity lifecycle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detailed);

        if(getIntent() != null ) {
            if(getIntent().getExtras() != null) {
                int tvId = getIntent().getExtras().getInt(EXTRA_ID);
                initUI();
                loadTvData(tvId);
            }
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            Log.e("PROBLEMS","Item id: " + item.getItemId() + ": android.R.id.home :" + android.R.id.home);
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

        backdrop = (ImageView) findViewById(R.id.detailedBackdrop);
        poster = (ImageView) findViewById(R.id.detailedPoster);
        overview = (TextView) findViewById(R.id.detailedOverview);
        year = (TextView) findViewById(R.id.detailedYear);
        budget = (TextView) findViewById(R.id.detailedBudget);
        popularity = (TextView) findViewById(R.id.detailedPopularity);
        createdBy = (TextView) findViewById(R.id.detailedCreatedBy);
        vote = (TextView) findViewById(R.id.detailedVote);
        genres = (TextView) findViewById(R.id.detailedGenres);
        tagline = (TextView) findViewById(R.id.detailedTagline);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.castRecycler);

    }

    /*Load data*/

    private void loadTvData(int tvId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<Tv> call = apiService.getTvDetails(tvId, getString(R.string.api_key), "ru");

        call.enqueue(new retrofit2.Callback<Tv>() {

            @Override
            public void onResponse(retrofit2.Call<Tv> call, retrofit2.Response<Tv> response) {
                if(response.body() != null) {
                    Tv responseTv = response.body();
                    updateInfo(responseTv, new ArrayList<Cast>());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Tv> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());

            }

        });
    }

    /*Additional methods*/

    private void updateInfo(Tv tv, final List<Cast> cast) {

        if(tv != null) {

            Picasso.with(this).load(getString(R.string.image_base) + tv.getBackdropPath()).into(backdrop);
            Picasso.with(this).load(getString(R.string.image_base) + tv.getPosterPath()).into(poster);

            overview.setText(tv.getOverview());
            collapsingToolbarLayout.setTitle(tv.getName());
            year.setText(tv.getFirstAirDate());
            vote.setText(tv.getVoteAverage() + "");
            popularity.setText(tv.getPopularity() + "");
            tagline.setText(tv.getHomepage());

            if(cast != null) {
                profileAdapter = new ProfileAdapter(this, cast);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                mRecyclerView.setAdapter(profileAdapter);
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(TvDetailedActivity.this, PersonDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(EXTRA_PERSON_ID, cast.get(position).getId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(
                                    TvDetailedActivity.this,
                                    "Unable to load information at {" + position + "} pos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));

            }
        } else {
            Toast.makeText(this, R.string.parse_error, Toast.LENGTH_LONG).show();
        }
    }

}
