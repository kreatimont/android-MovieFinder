package io.kreatimont.cinematograph.api;

import io.kreatimont.cinematograph.api.model.response.PersonResponse;
import io.kreatimont.cinematograph.api.model.response.TvResponse;
import io.kreatimont.cinematograph.api.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.api.model.tmdb.people.Person;
import io.kreatimont.cinematograph.api.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.api.model.response.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /*Movie*/

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang, @Query("append_to_response") String appendToResponse);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    @GET("movie/{id}/similar")
    Call<MoviesResponse> getSimilarMovies(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang,@Query("page") int page);

    /*TV*/

    @GET("tv/{id}")
    Call<Tv> getTvDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang, @Query("append_to_response") String appendToResponse);

    @GET("tv/top_rated")
    Call<TvResponse> getTopRatedTv(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    @GET("tv/popular")
    Call<TvResponse> getPopularTv(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    @GET("tv/{id}/similar")
    Call<TvResponse> getSimilarTv(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang,@Query("page") int page);

    /*Person*/

    @GET("person/popular")
    Call<PersonResponse> getPopularPersons(@Query("api_key") String apiKey, @Query("language") String lang, @Query("page") int page);

    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String lang);

    /*Search*/

    @GET("search/movie")
    Call<MoviesResponse> getMoviesByQuery(@Query("api_key") String apiKey, @Query("language") String lang, @Query("query") String query);

    @GET("search/tv")
    Call<TvResponse> getTvByQuery(@Query("api_key") String apiKey, @Query("language") String lang, @Query("query") String query);

    @GET("search/tv")
    Call<PersonResponse> getPersonsByQuery(@Query("api_key") String apiKey, @Query("language") String lang, @Query("query") String query);

    /*Discover*/

    @GET("discover/movie")
    Call<MoviesResponse> getDiscoverMovies(@Query("api_key") String apiKey, @Query("language") String lang, @Query("with_people") String withPeople,
                                           @Query("year") Integer year, @Query("page") Integer page);

    @GET("discover/tv")
    Call<TvResponse> getDiscoverTv(@Query("api_key") String apiKey, @Query("language") String lang, @Query("with_people") String withPeople,
                                           @Query("year") Integer year, @Query("page") Integer page);

}
