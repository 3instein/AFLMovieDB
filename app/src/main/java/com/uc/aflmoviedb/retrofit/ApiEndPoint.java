package com.uc.aflmoviedb.retrofit;

import com.uc.aflmoviedb.model.Credit;
import com.uc.aflmoviedb.model.Genre;
import com.uc.aflmoviedb.model.Movie;
import com.uc.aflmoviedb.model.NowPlaying;
import com.uc.aflmoviedb.model.Popular;
import com.uc.aflmoviedb.model.Search;
import com.uc.aflmoviedb.model.Similar;
import com.uc.aflmoviedb.model.Upcoming;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndPoint {

    @GET("movie/now_playing")
    Call<NowPlaying> getNowPlaying(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Call<Upcoming> getUpcoming(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("movie/popular")
    Call<Popular> getPopular(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("genre/movie/list")
    Call<Genre> getGenres(
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/similar")
    Call<Similar> getSimilar(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/credits")
    Call<Credit> getCredits(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

    @GET("search/movie")
    Call<Search> getSearch(
            @Query("query") String query,
            @Query("api_key") String apiKey
    );

}
