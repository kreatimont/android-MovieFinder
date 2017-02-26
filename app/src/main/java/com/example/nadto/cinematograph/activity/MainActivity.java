package com.example.nadto.cinematograph.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.fragment.ListFragment;
import com.example.nadto.cinematograph.fragment.MovieFragment;
import com.example.nadto.cinematograph.fragment.SearchFragment;
import com.example.nadto.cinematograph.fragment.SeriesFragment;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.loadMovies) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initUI() {

        currentFragment = null;

        final NavigationView nav = (NavigationView) findViewById(R.id.nav_view);

        final DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav, R.string.close_nav);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                ListFragment fragment = null;
                Class fragmentClass = null;

                if(itemId == R.id.movies) {
                    fragmentClass = MovieFragment.class;
                }
                if(itemId == R.id.series) {
                    fragmentClass = SeriesFragment.class;
                }
                if(itemId == R.id.persons) {

                }
                if(itemId == R.id.search) {
                    fragmentClass = SearchFragment.class;
                }

                item.setChecked(true);
                setTitle(item.getTitle());

                try {
                    fragment = (ListFragment)fragmentClass.newInstance();
                    currentFragment = fragment;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if(fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Replace with your own action", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
