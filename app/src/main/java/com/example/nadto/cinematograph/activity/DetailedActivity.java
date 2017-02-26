package com.example.nadto.cinematograph.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadto.cinematograph.fragment.Client;
import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.HttpHelper.LoadImageTask;
import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.R;

import org.json.JSONObject;

public class DetailedActivity extends AppCompatActivity implements Client {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TYPE = "type";

    JsonHelper jsonHelper;
    private ImageView backdrop, poster;
    private TextView overview, year, createdBy, budget, genres, popularity, vote;
    private int filmId;
    private int filmType;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        filmId = getIntent().getExtras().getInt(EXTRA_ID);
        filmType = getIntent().getExtras().getInt(EXTRA_TYPE);

        Log.e("onCreateDetailsActivity","ID: " + filmId + "; TYPE:" + filmType);

        initUI();

        jsonHelper = new JsonHelper(this);
        jsonHelper.loadJson(jsonHelper.createURL(filmId,filmType));
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

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFavorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

    }

    private void updateInfo(Film film) {

        if(film != null) {

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


        } else {
            Toast.makeText(this, R.string.parse_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setData(JSONObject jsonObject) {
        Film film = jsonHelper.convertJsonToFilm(jsonObject);
        this.updateInfo(film);
    }

}
