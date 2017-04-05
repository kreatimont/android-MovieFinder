package com.example.nadto.cinematograph.api;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiManager {

    private static ApiManager instance;
    private static OkHttpClient httpClient = new OkHttpClient();

    public static ApiManager getInstance() {
        if(instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public void loadFilm(String url, final ApiListener listener, Context context) {

        final JsonHelper jsonHelper = new JsonHelper(context);

        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.connectionError();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String responseData = response.body().string();
                    try {
                        listener.successfullyLoadFilm(
                                jsonHelper.convertJsonToFilm(new JSONObject(responseData)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        listener.parseError();
                    }
                }
            }
        });
    }

    public void loadFilmList(String url, final ApiListener listener, Context context) {

        final JsonHelper jsonHelper = new JsonHelper(context);

        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.connectionError();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String responseData = response.body().string();
                    try {
                        listener.successfullyLoadFilmList(
                                jsonHelper.convertJsonToFilmList(new JSONObject(responseData)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        listener.parseError();
                    }
                }
            }
        });
    }

}
