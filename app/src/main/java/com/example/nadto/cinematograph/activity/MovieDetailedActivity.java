package com.example.nadto.cinematograph.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.api.JsonHelper;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.ProfileAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.db.DataBaseHelper;
import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.model.Person;
import com.example.nadto.cinematograph.model.response.MoviesResponse;
import com.example.nadto.cinematograph.model.tmdb_model.credits.Cast;
import com.example.nadto.cinematograph.model.tmdb_model.movie.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.nadto.cinematograph.activity.MainActivity.APP_PREFERENCE;
import static com.example.nadto.cinematograph.activity.ProfileDetailedActivity.EXTRA_PROFILE_ID;

public class MovieDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private ImageView backdrop, poster;
    private TextView overview, year, createdBy, budget, genres, popularity, vote, tagline;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProfileAdapter profileAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fabFavorite;

    private OkHttpClient httpClient;

    /*Activity lifecycle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        if(getIntent() != null ) {
            if(getIntent().getExtras() != null) {
                int filmId = getIntent().getExtras().getInt(EXTRA_ID);
                initUI();
                loadData(filmId);
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

    private void updateInfo(Movie film, final List<Cast> cast) {

        if(film != null) {

            Picasso.with(this).load(getString(R.string.image_base) + film.getBackdropPath()).into(backdrop);
            Picasso.with(this).load(getString(R.string.image_base) + film.getPosterPath()).into(poster);

            overview.setText(film.getOverview());
            collapsingToolbarLayout.setTitle(film.getTitle());
            year.setText(film.getReleaseDate());
            genres.setText(film.getGenres().get(0).getName());
            createdBy.setText(film.getProductionCompanies().get(0).getName());
            vote.setText(film.getVoteAverage() + "");
            popularity.setText(film.getPopularity() + "");
            budget.setText(film.getBudget() + "");
            tagline.setText(getString(R.string.tagline_template, film.getTagline()));

            if(cast != null) {
                profileAdapter = new ProfileAdapter(this, cast);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                mRecyclerView.setAdapter(profileAdapter);
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MovieDetailedActivity.this, ProfileDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(EXTRA_PROFILE_ID, cast.get(position).getId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(
                                    MovieDetailedActivity.this,
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

    /*Load data*/

    private void loadData(int filmId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<Movie> call = apiService.getMovieDetails(filmId, getString(R.string.api_key), "ru", "credits");

        call.enqueue(new retrofit2.Callback<Movie>() {

            @Override
            public void onResponse(retrofit2.Call<Movie> call, retrofit2.Response<Movie> response) {
                Movie responseMovie = response.body();
                updateInfo(responseMovie, responseMovie.getCredits().getCast());
            }

            @Override
            public void onFailure(retrofit2.Call<Movie> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());

            }

        });
    }

}
