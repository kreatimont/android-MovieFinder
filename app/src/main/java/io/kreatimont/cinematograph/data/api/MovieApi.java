package io.kreatimont.cinematograph.data.api;


import io.kreatimont.cinematograph.data.model.response.MoviesResponse;
import io.kreatimont.cinematograph.data.model.tmdb.movie.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id,
                                @Query("api_key") String apiKey,
                                @Query("language") String lang,
                                @Query("append_to_response") String appendToResponse);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey,
                                           @Query("language") String lang,
                                           @Query("page") int page);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey,
                                          @Query("language") String lang,
                                          @Query("page") int page);

    @GET("movie/{id}/similar")
    Call<MoviesResponse> getSimilarMovies(@Path("id") int id,
                                          @Query("api_key") String apiKey,
                                          @Query("language") String lang,
                                          @Query("page") int page);


}
