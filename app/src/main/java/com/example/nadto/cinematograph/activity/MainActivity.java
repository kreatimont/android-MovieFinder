package com.example.nadto.cinematograph.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.fragment.MovieFragment;
import com.example.nadto.cinematograph.fragment.ProtoFragment;
import com.example.nadto.cinematograph.fragment.TvFragment;
import com.example.nadto.cinematograph.utils.InternetConnection;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private ProtoFragment currentFragment;

    /*Activity lifecycle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        if(!InternetConnection.isConnected(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        initUI();
        initDB();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(currentFragment != null) {
                    currentFragment.handleSearchQuery(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(currentFragment != null) {
                    currentFragment.handleSearchQuery(newText);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.grid2) {
            //currentFragment.setLayoutManager( new GridLayoutManager(this, 2));
        }

        if(id == R.id.grid3) {
            //currentFragment.setLayoutManager( new GridLayoutManager(this, 3));
        }

        if(id == R.id.grid4) {
            //currentFragment.setLayoutManager( new GridLayoutManager(this, 4));
        }

        if(id == R.id.linear) {
            //currentFragment.setLayoutManager(new LinearLayoutManager(this));
        }

        if(id == R.id.search) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /*Additional methods*/

    public void initUI() {

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

                Fragment fragment = null;

                if(itemId == R.id.movies) {
                    fragment = new MovieFragment();
                }
                if(itemId == R.id.series) {
                    fragment = new TvFragment();
                }
                if(itemId == R.id.persons) {
                    Toast.makeText(MainActivity.this, "Open category is failed.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(itemId == R.id.favorite) {
                    return true;
                }

                item.setChecked(true);
                setTitle(item.getTitle());

                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                currentFragment = (ProtoFragment) fragment;

                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        nav.setCheckedItem(R.id.favorite);
    }

    private void initDB() {
        Realm.init(this);
    }

}
