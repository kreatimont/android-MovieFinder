package com.example.nadto.cinematograph.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadto.cinematograph.adapter.ProfileAdapter;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;
import com.example.nadto.cinematograph.db.DataBaseHelper;
import com.example.nadto.cinematograph.fragment.Client;
import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.HttpHelper.LoadImageTask;
import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.model.Person;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.nadto.cinematograph.activity.MainActivity.APP_PREFERENCE;
import static com.example.nadto.cinematograph.activity.ProfileDetailedActivity.EXTRA_PROFILE_ID;

public class FilmDetailedActivity extends AppCompatActivity implements Client {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TYPE = "type";

    JsonHelper jsonHelper;
    private ImageView backdrop, poster;
    private TextView overview, year, createdBy, budget, genres, popularity, vote, tagline;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProfileAdapter profileAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fabFavorite;
    private Film film;
    private boolean isFavorite;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        if(getIntent() != null ) {
            if(getIntent().getExtras() != null) {
                int filmId = getIntent().getExtras().getInt(EXTRA_ID);
                int filmType = getIntent().getExtras().getInt(EXTRA_TYPE);
                film = new Film(filmId, filmType);
                Log.e("onCreateDetailsActivity","ID: " + filmId + "; TYPE:" + filmType);

                initUI();

                jsonHelper = new JsonHelper(this);
                jsonHelper.loadJson(jsonHelper.createURL(filmId, filmType));
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

    private void initUI() {

        dataBaseHelper = new DataBaseHelper(FilmDetailedActivity.this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataBaseHelper.isExistFilm(film.getId(),film.getType())) {
                    dataBaseHelper.deleteFilm(film.getId(), film.getType());
                    fabFavorite.setImageResource(R.drawable.ic_heart);
                } else {
                    dataBaseHelper.addFilm(new Film(film.getId(),film.getType()));
                    fabFavorite.setImageResource(R.drawable.ic_hear_fill);
                }
            }
        });

        if(dataBaseHelper.isExistFilm(film.getId(), film.getType())) {
            fabFavorite.setImageResource(R.drawable.ic_hear_fill);
        }

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

    private void updateInfo(Film film, final ArrayList<Person> cast) {

        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCE,Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt(getString(R.string.saved_id), 44217);
        int type = sharedPreferences.getInt(getString(R.string.saved_type), Film.TV);

        if(film != null) {

            if(film.getId() == id && film.getType() == type) {
                fabFavorite.setImageResource(R.drawable.ic_hear_fill);
                isFavorite = true;
            }

            new LoadImageTask(backdrop).execute(film.getPathToBackdrop());
            new LoadImageTask(poster).execute(film.getPathToPoster());

            overview.setText(film.getOverview());
            collapsingToolbarLayout.setTitle(film.getTitle());
            year.setText(film.getDate());
            genres.setText(film.getGenres());
            createdBy.setText(film.getCreatedBy());
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
                        Intent intent = new Intent(FilmDetailedActivity.this, ProfileDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(EXTRA_PROFILE_ID, cast.get(position).getId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(
                                    FilmDetailedActivity.this,
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

    @Override
    public void setData(JSONObject jsonObject) {
        film = jsonHelper.convertJsonToFilm(jsonObject);
        ArrayList<Person> cast = jsonHelper.getCreditsFromJsonObject(jsonObject);
        this.updateInfo(film, cast);
    }

}
