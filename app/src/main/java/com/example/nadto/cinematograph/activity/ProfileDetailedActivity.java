package com.example.nadto.cinematograph.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.HttpHelper.LoadImageTask;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.fragment.Client;
import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.model.Person;

import org.json.JSONObject;

public class ProfileDetailedActivity extends AppCompatActivity implements Client {

    public static final String EXTRA_PROFILE_ID = "id";

    private JsonHelper jsonHelper;

    private TextView name, biography, gender, birthday, placeOfBirth, link;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detailed);

        int id = getIntent().getIntExtra(EXTRA_PROFILE_ID, 0);

        Log.e("onCreateProfileActivity","Person id: " + id);

        initUI();

        jsonHelper = new JsonHelper(this);
        jsonHelper.loadJson(jsonHelper.createURL(id, Film.PERSON));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUI() {

        name = (TextView) findViewById(R.id.detProfileName);
        biography = (TextView) findViewById(R.id.detProfileBiography);
        gender = (TextView) findViewById(R.id.detProfileGender);
        birthday = (TextView) findViewById(R.id.detProfileBirthday);
        placeOfBirth = (TextView) findViewById(R.id.detProfilePlaceOfBirth);
        link = (TextView) findViewById(R.id.detProfileLink);
        profilePhoto = (ImageView) findViewById(R.id.detProfilePhoto);

    }

    @Override
    public void setData(JSONObject jsonObject) {
        Person person = jsonHelper.convertJsonToPerson(jsonObject);
        if(person != null) {
            name.setText(person.getName());
            biography.setText(person.getBiography());
            gender.setText(person.getGender());
            birthday.setText(person.getBirthday());
            placeOfBirth.setText(person.getPlaceOfBirth());
            link.setText(person.getLink());
            new LoadImageTask(profilePhoto).execute(person.getProfilePath());
            getSupportActionBar().setTitle(person.getName());
        }
    }

    public void onSiteLinkClick(View view) {
        String url = ((TextView)view).getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


}
