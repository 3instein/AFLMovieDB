package com.uc.aflmoviedb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.uc.aflmoviedb.model.Credit;
import com.uc.aflmoviedb.model.Genre;
import com.uc.aflmoviedb.model.Movie;
import com.uc.aflmoviedb.model.NowPlaying;
import com.uc.aflmoviedb.model.Popular;
import com.uc.aflmoviedb.model.Search;
import com.uc.aflmoviedb.model.Similar;
import com.uc.aflmoviedb.model.Upcoming;
import com.uc.aflmoviedb.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance();
    }

    private MutableLiveData<NowPlaying> resultGetNowPlaying = new MutableLiveData<>();
    public void getNowPlaying(int page) {
        resultGetNowPlaying = repository.getNowPlayingData(page);
    }
    public LiveData<NowPlaying> getResultNowPlaying() {
        return resultGetNowPlaying;
    }

    public MutableLiveData<Upcoming> resultGetUpcoming = new MutableLiveData<>();
    public void getUpcoming(int page) {
        resultGetUpcoming = repository.getUpcomingData(page);
    }
    public LiveData<Upcoming> getResultUpcoming() {
        return resultGetUpcoming;
    }

    public MutableLiveData<Popular> resultGetPopular = new MutableLiveData<>();
    public void getPopular(int page) {
        resultGetPopular = repository.getPopularData(page);
    }
    public LiveData<Popular> getResultPopular() {
        return resultGetPopular;
    }

    private MutableLiveData<List<Genre.Genres>> resultGetGenres = new MutableLiveData<>();
    public void getGenres(List<Integer> ids) {
        resultGetGenres = repository.getMovieGenres(ids);
    }
    public LiveData<List<Genre.Genres>> getResultGenres(){
        return resultGetGenres;
    }

    private MutableLiveData<Similar> resultGetSimilar = new MutableLiveData<>();
    public void getSimilar(int movie_id) {
        resultGetSimilar = repository.getSimilarData(movie_id);
    }
    public LiveData<Similar> getResultSimilar(){
        return resultGetSimilar;
    }

    private MutableLiveData<Credit> resultGetCredit = new MutableLiveData<>();
    public void getCredit(int movie_id){
        resultGetCredit = repository.getCreditData(movie_id);
    }
    public LiveData<Credit> getResultCredit(){
        return resultGetCredit;
    }

    private MutableLiveData<Movie> resultGetMovie = new MutableLiveData<>();
    public void getMovie(int movie_id){
        resultGetMovie = repository.getMovieData(movie_id);
    }
    public LiveData<Movie> getResultMovie(){
        return resultGetMovie;
    }

    private MutableLiveData<Search> resultGetSearch = new MutableLiveData<>();
    public void getSearch(String query){
        resultGetSearch = repository.getSearchData(query);
    }
    public LiveData<Search> getResultSearch(){
        return resultGetSearch;
    }

}
