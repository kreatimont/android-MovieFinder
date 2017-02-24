package com.example.nadto.cinematograph;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    public static final String TITLE = "title";

    private ImageView backdrop;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();

        String strTitle = getIntent().getStringExtra(TITLE);
//        title.setText(strTitle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFavorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(strTitle);
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

        backdrop = (ImageView) findViewById(R.id.detailedBackdrop);
//        title = (TextView) findViewById(R.id.detailedTitle);



    }

}
