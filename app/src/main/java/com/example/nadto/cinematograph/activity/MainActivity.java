package com.example.nadto.cinematograph.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.fragment.MovieFragment;
import com.example.nadto.cinematograph.fragment.TvFragment;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCE = "app_pref0451";
    public static final int WIRELESS_RESULT_CODE = 1337;
    public static final String SS_TITLE = "ss_title";


    public static final String FAVORITES = "fav";
    public static final String MOVIES = "mov";
    public static final String SERIES = "ser";

    private View rootView;

    private boolean mReturningWithResult = false;

    /*Activity lifecyle*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        rootView = findViewById(R.id.content_main);

        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString(SS_TITLE));
        }

        checkNetwork();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(mReturningWithResult) {
            initUI();
        }
        mReturningWithResult = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mReturningWithResult = true;
        if(requestCode == WIRELESS_RESULT_CODE) {
           checkNetwork();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //currentFragment.loadItemListFromUrl(createSearchURL(query),true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

        Log.e("TAG","init UI");

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

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();


                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        nav.setCheckedItem(R.id.favorite);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private void checkNetwork() {
        if(isOnline(this)) {
            if(!mReturningWithResult) {
                initUI();
            }
        } else {
            Snackbar.make(rootView, R.string.network_failed, Snackbar.LENGTH_LONG)
                    .setAction("Turn on", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), WIRELESS_RESULT_CODE);
                        }
                    })
                    .show();
        }
    }

}
