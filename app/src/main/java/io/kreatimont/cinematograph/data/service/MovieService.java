package io.kreatimont.cinematograph.data.service;

import com.google.gson.GsonBuilder;

import io.kreatimont.cinematograph.data.api.MovieApi;
import io.kreatimont.cinematograph.data.model.response.MoviesResponse;
import io.kreatimont.cinematograph.utils.Cinematograph;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieService {

    private Call call;
    private String apiKey;
    private String language;

    public void requestPopularMovies(int page, final OnRequestAllMoviesListener listener) {
        Call<MoviesResponse> getPopularMovies = buildApi(buildClient()).getPopularMovies(apiKey, language, page);
        getPopularMovies.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (listener != null) {
                    listener.onRequestAllMoviesSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if(listener != null) {
                    listener.onRequestAllMoviesFailed(call, t);
                }
            }
        });
    }

    private MovieService(String apiKey, String language) {
        this.apiKey = apiKey;
        this.language = language;
    }

    public static MovieService getService(String apiKey, String language) {
        return new MovieService(apiKey, language);
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    private MovieApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Cinematograph.TMDB_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create((MovieApi.class));
    }



    /*callbacks*/

    public interface OnRequestAllMoviesListener {
        void onRequestAllMoviesSuccess(Call<MoviesResponse> call, Response<MoviesResponse> response);
        void onRequestAllMoviesFailed(Call<MoviesResponse> call, Throwable throwable);
    }

}
