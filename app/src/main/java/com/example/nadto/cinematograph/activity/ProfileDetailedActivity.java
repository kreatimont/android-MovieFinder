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

import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.model.Person;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileDetailedActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "id";

    private JsonHelper jsonHelper;
    private OkHttpClient httpClient;

    private TextView name, biography, gender, birthday, placeOfBirth, link;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detailed);

        int id = getIntent().getIntExtra(EXTRA_PROFILE_ID, 0);

        Log.e("onCreateProfileActivity","Person id: " + id);

        initUI();

        httpClient = new OkHttpClient();

        jsonHelper = new JsonHelper(this);
        loadItemFromUrl(jsonHelper.createURL(id, Film.PERSON).toString());
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

    public void setData(Person person) {
        if(person != null) {
            name.setText(person.getName());
            biography.setText(person.getBiography());
            gender.setText(person.getGender());
            birthday.setText(person.getBirthday());
            placeOfBirth.setText(person.getPlaceOfBirth());
            link.setText(person.getLink());
            Picasso.with(this).load(person.getProfilePath()).into(profilePhoto);
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


    public void loadItemFromUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String responseData = response.body().string();
                    ProfileDetailedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Person person = jsonHelper.convertJsonToPerson(new JSONObject(responseData));
                                setData(person);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

}
