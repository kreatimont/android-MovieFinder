package io.kreatimont.cinematograph.ui.tv;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import io.kreatimont.cinematograph.ui.person.CastAdapter;
import io.kreatimont.cinematograph.helpers.RecyclerItemClickListener;
import io.kreatimont.cinematograph.api.ApiClient;
import io.kreatimont.cinematograph.api.ApiInterface;
import io.kreatimont.cinematograph.api.model.tmdb.Genre;
import io.kreatimont.cinematograph.api.model.tmdb.credits.Cast;
import io.kreatimont.cinematograph.api.model.tmdb.people.Person;
import io.kreatimont.cinematograph.api.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.ui.main.MainActivity;
import io.kreatimont.cinematograph.ui.person.PersonDetailedActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TvDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private ImageView backdrop, poster;
    private TextView overview, year, createdBy, genres, popularity, vote, tagline, title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CastAdapter castAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fabFavorite;
    private LinearLayout overviewForm;


    /*Activity lifecycle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_tv);

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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Configure UI*/

    private void initUI() {

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.detailedCoordinatorLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overviewForm = (LinearLayout) findViewById(R.id.overviewForm);
        title = (TextView) findViewById(R.id.detailedTitle);
        genres = (TextView) findViewById(R.id.detailedGenres);
        backdrop = (ImageView) findViewById(R.id.detailedBackdrop);
        poster = (ImageView) findViewById(R.id.detailedPoster);
        overview = (TextView) findViewById(R.id.detailedOverview);
        year = (TextView) findViewById(R.id.detailedYear);

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

        retrofit2.Call<Tv> call = apiService.getTvDetails(tvId, getString(R.string.api_key), "ru", "credits");
        call.enqueue(new retrofit2.Callback<Tv>() {

            @Override
            public void onResponse(retrofit2.Call<Tv> call, retrofit2.Response<Tv> response) {
                if(response.body() != null) {
                    Tv responseTv = response.body();
                    updateInfo(responseTv, responseTv.getCredits().getCast());
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

            collapsingToolbarLayout.setTitle(tv.getName());
            collapsingToolbarLayout.setExpandedTitleMarginBottom(-999);

            Picasso.with(this).load(getString(R.string.image_base) + getString(R.string.backdrop_size_big) + tv.getBackdropPath()).into(backdrop);
            Picasso.with(this).load(getString(R.string.image_base) + getString(R.string.poster_size_medium) + tv.getPosterPath()).into(poster);

            for(Genre g : tv.getGenres()) {
                genres.append(g.getName() + " ");
            }

            title.setText(tv.getName());

            if(tv.getOverview().length() < 1) {
                overviewForm.setVisibility(View.GONE);
            } else {
                overview.setText(tv.getOverview());
            }

            for(Person p : tv.getCreatedBy()) {
                createdBy.append(p.getName() + " | ");
            }

            year.setText(tv.getFirstAirDate());
            vote.setText(tv.getVoteAverage() + "");
            popularity.setText(tv.getPopularity() + "");
            tagline.setText(tv.getHomepage());

            if(cast != null) {
                castAdapter = new CastAdapter(this, cast);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                mRecyclerView.setAdapter(castAdapter);
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(TvDetailedActivity.this, PersonDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(PersonDetailedActivity.EXTRA_PERSON_ID, cast.get(position).getId());

                            /*clear activity stack*/
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
