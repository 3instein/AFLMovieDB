package com.uc.aflmoviedb.repositories;

import androidx.lifecycle.MutableLiveData;

import com.uc.aflmoviedb.helper.Const;
import com.uc.aflmoviedb.model.Credit;
import com.uc.aflmoviedb.model.Genre;
import com.uc.aflmoviedb.model.Movie;
import com.uc.aflmoviedb.model.NowPlaying;
import com.uc.aflmoviedb.model.Popular;
import com.uc.aflmoviedb.model.Search;
import com.uc.aflmoviedb.model.Similar;
import com.uc.aflmoviedb.model.Upcoming;
import com.uc.aflmoviedb.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static MovieRepository repository;

    private MovieRepository(){

    }

    public static MovieRepository getInstance(){
        if(repository == null){
            repository = new MovieRepository();
        }
        return repository;
    }

    public MutableLiveData<NowPlaying> getNowPlayingData(int page){
        final MutableLiveData<NowPlaying> result = new MutableLiveData<>();

        ApiService.endPoint().getNowPlaying(page, Const.API_KEY).enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {

            }
        });

        return result;
    }

    public MutableLiveData<Upcoming> getUpcomingData(int page){
        final MutableLiveData<Upcoming> result = new MutableLiveData<>();

        ApiService.endPoint().getUpcoming(page, Const.API_KEY).enqueue(new Callback<Upcoming>() {
            @Override
            public void onResponse(Call<Upcoming> call, Response<Upcoming> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Upcoming> call, Throwable t) {

            }
        });

        return result;
    }

    public MutableLiveData<Popular> getPopularData(int page){
        final MutableLiveData<Popular> result = new MutableLiveData<>();

        ApiService.endPoint().getPopular(page, Const.API_KEY).enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Popular> call, Throwable t) {

            }
        });

        return result;
    }

    public MutableLiveData<List<Genre.Genres>> getMovieGenres(List<Integer> ids){
        final MutableLiveData<List<Genre.Genres>> result = new MutableLiveData<>();

        ApiService.endPoint().getGenres(Const.API_KEY).enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                List<Genre.Genres> temp = new ArrayList<>();
                for (int id : ids){
                    for (Genre.Genres item : response.body().getGenres()){
                        if(item.getId() == id){
                            temp.add(item);
                        }
                    }
                }

                result.setValue(temp);
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                System.err.println(t);
            }
        });

        return  result;
    }

    public MutableLiveData<Similar> getSimilarData(int movie_id){
        final MutableLiveData<Similar> result = new MutableLiveData<>();

        ApiService.endPoint().getSimilar(movie_id, Const.API_KEY).enqueue(new Callback<Similar>() {
            @Override
            public void onResponse(Call<Similar> call, Response<Similar> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Similar> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return result;
    }

    public MutableLiveData<Credit> getCreditData(int movie_id){
        final MutableLiveData<Credit> result = new MutableLiveData<>();

        ApiService.endPoint().getCredits(movie_id, Const.API_KEY).enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return result;
    }

    public MutableLiveData<Movie> getMovieData(int movie_id){
        final MutableLiveData<Movie> result = new MutableLiveData<>();

        ApiService.endPoint().getMovieDetails(movie_id, Const.API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return result;
    }

    public MutableLiveData<Search> getSearchData(String query){
        final MutableLiveData<Search> result = new MutableLiveData<>();

        ApiService.endPoint().getSearch(query, Const.API_KEY).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });

        return result;
    }
}


