package com.example.nadto.cinematograph.api;

import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.model.response.MoviesResponse;
import com.example.nadto.cinematograph.model.response.TvResponse;
import com.example.nadto.cinematograph.model.tmdb_model.People.Person;
import com.example.nadto.cinematograph.model.tmdb_model.movie.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /*Movie*/

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang, @Query("append_to_response") String appendToResponse);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey,@Query("language") String lang, @Query("page") int page);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey,@Query("language") String lang, @Query("page") int page);

    /*TV*/

    @GET("tv/{id}")
    Call<Film> getTvDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang);

    @GET("tv/top_rated")
    Call<TvResponse> getTopRatedTv(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    @GET("tv/popular")
    Call<TvResponse> getPopularTv(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    /*Person*/

    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang);

}
