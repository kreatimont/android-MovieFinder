package io.kreatimont.cinematograph.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class Cinematograph extends Application {

    public static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";

    private static Cinematograph instance;

    public static Cinematograph getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        Log.e("APPLICATION: ","Instance created");
        super.onCreate();
    }
}
