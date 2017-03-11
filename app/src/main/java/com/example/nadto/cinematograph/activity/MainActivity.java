package com.example.nadto.cinematograph.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.fragment.FavoriteFragment;
import com.example.nadto.cinematograph.fragment.ListFragment;
import com.example.nadto.cinematograph.fragment.MovieFragment;
import com.example.nadto.cinematograph.fragment.SearchFragment;
import com.example.nadto.cinematograph.fragment.SeriesFragment;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCE = "app_pref0451";
    public static final int WIRELESS_RESULT_CODE = 1337;
    public static final String SS_TITLE = "ss_title";

    private View rootView;

    private ListFragment currentFragment;
    private int currentMoviePage = 1;
    private int currentTvPage = 1;

    private boolean mReturningWithResult = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "current-fragment", currentFragment);
        outState.putString(SS_TITLE,getTitle().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        rootView = findViewById(R.id.content_main);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            currentFragment = (ListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "current-fragment");
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.grid2) {
            currentFragment.setLayoutManager( new GridLayoutManager(this, 2));
        }

        if(id == R.id.grid3) {
            currentFragment.setLayoutManager( new GridLayoutManager(this, 3));
        }

        if(id == R.id.grid4) {
            currentFragment.setLayoutManager( new GridLayoutManager(this, 4));
        }

        if(id == R.id.linear) {
            currentFragment.setLayoutManager(new LinearLayoutManager(this));
        }

        return super.onOptionsItemSelected(item);
    }

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
                if(itemId == R.id.favorite) {
                    fragmentClass = FavoriteFragment.class;
                }

                item.setChecked(true);
                setTitle(item.getTitle());

                try {
                    fragment = (ListFragment)fragmentClass.newInstance();
                    currentFragment = fragment;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if(currentFragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, currentFragment).commit();
                } else {
                    Toast.makeText(MainActivity.this, "Open category is failed.", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });


        if(currentFragment == null) {
            try {
                currentFragment = FavoriteFragment.class.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            nav.setCheckedItem(R.id.favorite);
            setTitle(R.string.favorite);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, currentFragment).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(currentFragment instanceof MovieFragment) {
                    currentMoviePage++;
                    currentFragment.setQuery(
                            getString(R.string.base_url_movie)
                                    + getString(R.string.mode_popular)
                                    + getString(R.string.api_key_prefix)
                                    + getString(R.string.api_key)
                                    + getString(R.string.mode_page, currentMoviePage));
                    currentFragment.setUpData();
                    Toast.makeText(MainActivity.this, "Page : " + currentMoviePage, Toast.LENGTH_SHORT).show();
                } else if (currentFragment instanceof SeriesFragment) {
                    currentTvPage++;
                    String query =
                            getString(R.string.base_url_tv)
                                    + getString(R.string.mode_popular)
                                    + getString(R.string.api_key_prefix)
                                    + getString(R.string.api_key)
                                    + getString(R.string.mode_page, currentTvPage);
                    currentFragment.setQuery(
                            getString(R.string.base_url_tv)
                                    + getString(R.string.mode_popular)
                                    + getString(R.string.api_key_prefix)
                                    + getString(R.string.api_key)
                                    + getString(R.string.mode_page, currentTvPage));
                    currentFragment.setUpData();
                    Toast.makeText(MainActivity.this, "Page : " + currentTvPage, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Not available for this category", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private void checkNetwork() {
        Log.e("TAG","Now is check network");
        if(isOnline(this)) {
            Log.e("TAG","now is online");
            if(!mReturningWithResult) {
                initUI();
            }
        } else {
            Log.e("TAG","now is not online");
            Log.e("TAG","befor snackbar make");
            Snackbar.make(rootView, R.string.network_failed, Snackbar.LENGTH_LONG)
                    .setAction("Turn on", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("TAG","set action");
                            Log.e("TAG","Start activity for result");
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), WIRELESS_RESULT_CODE);
                        }
                    })
                    .show();
        }
    }

}
