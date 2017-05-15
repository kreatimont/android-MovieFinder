package io.kreatimont.cinematograph.ui.person;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import io.kreatimont.cinematograph.ui.proto.adapter.MovieCardLayoutType;
import io.kreatimont.cinematograph.ui.movie.MovieAdapter;
import io.kreatimont.cinematograph.helpers.RecyclerItemClickListener;
import io.kreatimont.cinematograph.ui.proto.adapter.ResponseRecyclerViewAdapter;
import io.kreatimont.cinematograph.ui.tv.TvAdapter;
import io.kreatimont.cinematograph.data.service.RetrofitClient;
import io.kreatimont.cinematograph.data.api.TMDbAPI;
import io.kreatimont.cinematograph.data.model.response.MoviesResponse;
import io.kreatimont.cinematograph.data.model.response.TvResponse;
import io.kreatimont.cinematograph.data.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.data.model.tmdb.people.Person;
import io.kreatimont.cinematograph.data.model.tmdb.tv.Tv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.kreatimont.cinematograph.ui.movie.MovieDetailedActivity;
import io.kreatimont.cinematograph.ui.tv.TvDetailedActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "id";

    private TextView name, biography, gender, birthday, placeOfBirth, link;
    private ImageView profilePhoto;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerViewMovies, mRecyclerViewTv;
    private ArrayList<Movie> mDataListMovies;
    private ArrayList<Tv> mDataListTv;
    private MovieAdapter mAdapterMovies;
    private TvAdapter mAdapterTv;

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

        mRecyclerViewMovies = (RecyclerView) findViewById(R.id.personMoviesRecycler);
        mRecyclerViewTv = (RecyclerView) findViewById(R.id.personTvRecycler);

        name = (TextView) findViewById(R.id.detailedTitle);
        gender = (TextView) findViewById(R.id.detailedGender);
        biography = (TextView) findViewById(R.id.detProfileBiography);
        birthday = (TextView) findViewById(R.id.detProfileBirthday);
        placeOfBirth = (TextView) findViewById(R.id.detProfilePlaceOfBirth);
        link = (TextView) findViewById(R.id.detProfileLink);
        profilePhoto = (ImageView) findViewById(R.id.detailedBackdrop);

        mRecyclerViewMovies.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        mRecyclerViewMovies, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(PersonDetailedActivity.this, MovieDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(MovieDetailedActivity.EXTRA_ID, (mDataListMovies.get(position)).getId());

                            /*clear activity stack*/
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        } else {
                            Snackbar.make(collapsingToolbarLayout, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                }));

        mRecyclerViewTv.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        mRecyclerViewTv, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(PersonDetailedActivity.this, TvDetailedActivity.class);
                        if(position >= 0) {
                            intent.putExtra(TvDetailedActivity.EXTRA_ID, (mDataListTv.get(position).getId()));

                            /*clear activity stack*/
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        } else {
                            Snackbar.make(collapsingToolbarLayout, "Unable to load information at {" + position + "} pos",Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                }));

        mDataListMovies = new ArrayList<>();
        mDataListTv = new ArrayList<>();

        mAdapterMovies = new MovieAdapter(this, mDataListMovies);
        mAdapterTv = new TvAdapter(this, mDataListTv);

        mRecyclerViewMovies.setAdapter(mAdapterMovies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerViewMovies.setLayoutManager(gridLayoutManager);
        ((ResponseRecyclerViewAdapter) mAdapterMovies).setLayout(MovieCardLayoutType.Grid);

        mRecyclerViewTv.setAdapter(mAdapterTv);
        GridLayoutManager gridLayoutManagerTv = new GridLayoutManager(this, 2);
        mRecyclerViewTv.setLayoutManager(gridLayoutManagerTv);
        ((ResponseRecyclerViewAdapter) mAdapterTv).setLayout(MovieCardLayoutType.Grid);

    }

    /*Content providers*/

    private void loadData(int personId) {

        TMDbAPI apiService = RetrofitClient.getClient().create(TMDbAPI.class);

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

        TMDbAPI apiService = RetrofitClient.getClient().create(TMDbAPI.class);

        retrofit2.Call<MoviesResponse> callMovie = apiService.getDiscoverMovies(getString(R.string.api_key), "ru", String.valueOf(personId), null, null);

        callMovie.enqueue(new retrofit2.Callback<MoviesResponse>() {

            @Override
            public void onResponse(retrofit2.Call<MoviesResponse> call, retrofit2.Response<MoviesResponse> response) {
                if(response.body() != null) {
                    Log.e("TAG MOVIE", call.request().toString());
                    List<Movie> responseMovies = response.body().getResults();

                    if(responseMovies.size() >= 4) {
                        mDataListMovies.addAll(responseMovies.subList(0,4));
                    } else {
                        mDataListMovies.addAll(responseMovies);
                    }

                    mAdapterMovies.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MoviesResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
            }

        });

        retrofit2.Call<TvResponse> callTv = apiService.getDiscoverTv(getString(R.string.api_key), "ru", String.valueOf(personId), null, null);

        callTv.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                List<Tv> responseTv= response.body().getResults();
                Log.e("TAG TV", call.request().toString());

                if(responseTv.size() >= 4) {
                    mDataListTv.addAll(responseTv.subList(0,4));
                } else {
                    mDataListTv.addAll(responseTv);
                }

                mAdapterTv.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e("Retrofit(failure)", t.getMessage());
            }
        });

    }

    /*Additional methods*/

    private void updateInfo(Person person) {
        if(person != null) {

            Picasso.with(this).load(getString(R.string.image_base) + getString(R.string.size_orginal) + person.getProfilePath()).into(profilePhoto);

            collapsingToolbarLayout.setExpandedTitleMarginBottom(-999);
            collapsingToolbarLayout.setTitle(person.getName());

            name.setText(person.getName());
            biography.setText(person.getBiography());
            gender.setText(person.getGender() == 2 ? "male" : "female");
            birthday.setText(person.getBirthday());
            placeOfBirth.setText(person.getPlaceOfBirth());
            link.setText(person.getHomepage());


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
