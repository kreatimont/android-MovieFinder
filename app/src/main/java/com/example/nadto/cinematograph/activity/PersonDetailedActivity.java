package com.example.nadto.cinematograph.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.api.ApiClient;
import com.example.nadto.cinematograph.api.ApiInterface;
import com.example.nadto.cinematograph.model.tmdb_model.people.Person;
import com.squareup.picasso.Picasso;

public class PersonDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "id";

    private TextView name, biography, gender, birthday, placeOfBirth, link;
    private ImageView profilePhoto;

    /*Activity lifecycle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detailed);

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

        name = (TextView) findViewById(R.id.detProfileName);
        biography = (TextView) findViewById(R.id.detProfileBiography);
        gender = (TextView) findViewById(R.id.detProfileGender);
        birthday = (TextView) findViewById(R.id.detProfileBirthday);
        placeOfBirth = (TextView) findViewById(R.id.detProfilePlaceOfBirth);
        link = (TextView) findViewById(R.id.detProfileLink);
        profilePhoto = (ImageView) findViewById(R.id.detProfilePhoto);

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

    /*Additional methods*/

    private void updateInfo(Person person) {
        if(person != null) {
            name.setText(person.getName());
            biography.setText(person.getBiography());
            gender.setText(person.getGender() == 0 ? "male" : "female");
            birthday.setText(person.getBirthday());
            placeOfBirth.setText(person.getPlaceOfBirth());
            link.setText(person.getHomepage());
            Picasso.with(this).load(getString(R.string.image_base) + person.getProfilePath()).into(profilePhoto);
            getSupportActionBar().setTitle(person.getName());
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
