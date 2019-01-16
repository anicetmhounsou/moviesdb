package com.hawksteam.movies.ui.movieslisting.movieslistingfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.hawksteam.movies.data.DataManager;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.utils.AppLogger;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MoviesListingFragmentViewModel extends ViewModel {

    private CompositeDisposable mCompositeDisposable;

    private MoviesListingFragmentCallbacks moviesListingFragmentCallbacks;

    private MutableLiveData<List<MovieEntity>> loadedMovies = new MutableLiveData<>();

    private boolean showingSearchResult = false;

    private int currentPage = 1;

    private DataManager mDataManager;

    public MoviesListingFragmentViewModel(MoviesListingFragmentCallbacks moviesListingFragmentCallbacks){
        this.moviesListingFragmentCallbacks = moviesListingFragmentCallbacks;

        mCompositeDisposable = new CompositeDisposable();

    }

    public void setView(){
        if(!showingSearchResult){
            displayMovies();
        }
    }

    public void setDataManager(DataManager dataManager){
        this.mDataManager = dataManager;
    }

    private void displayMovies(){
        showLoading();

        mCompositeDisposable.add(mDataManager.fetchMovies(currentPage,0)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(this::onMoviesFetchSuccess, this::onMoviesFetchFailed));

    }

    private void displayMovieSearchResult(@NonNull final String searchText) {
        showingSearchResult = true;
        showLoading();

        //local search, on failed, do remote search
        mCompositeDisposable.add(mDataManager.searchLocalMovie(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieEntities -> localMoviesearchSucccess(movieEntities)));

        /*mCompositeDisposable.add(mDataManager.searchLocalMovie(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableFlowableObserver<List<MovieEntity>>(){

                    @Override
                    public void onSuccess(List<MovieEntity> movieEntities) {

                        if(movieEntities.size()>0){
                            localMoviesearchSucccess(movieEntities);
                        }else{
                            localMovieSearchFailed(searchText);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.e(e.getMessage());
                    }
                }));*/




    }

    public void firstPage() {
        currentPage = 1;

        /*if(loadedMovies.getValue() != null){
            loadedMovies.getValue().clear();
        }*/

        displayMovies();
    }

    public void nextPage() {
        if(showingSearchResult)
            return;
        if (mDataManager.isPaginationSupported(0)) {
            currentPage++;
            displayMovies();
        }
    }

    public void searchMovie(final String searchText){
        if(searchText == null || searchText.length() < 1) {
            displayMovies();
        } else {
            displayMovieSearchResult(searchText);
        }
    }

    public void searchMovieBackPressed(){
        if(showingSearchResult) {
            showingSearchResult = false;
            //loadedMovies.getValue().clear();
            displayMovies();
        }
    }

    private void showLoading(){
        this.moviesListingFragmentCallbacks.loadingStarted();
    }

    private void onMoviesFetchSuccess(List<Movie> movies){

        /*if(mDataManager.isPaginationSupported(0)){
            if (loadedMovies.getValue() != null) {
                loadedMovies.getValue().addAll(movies);
            }else{
                loadedMovies.setValue(movies);
            }

        }else{
            loadedMovies.setValue(movies);
        }*/

        AppLogger.e("Saving Movies to room database");


        for (Movie movie :movies) {
           mCompositeDisposable.add(mDataManager.insertMovie(new MovieEntity(movie.getId(),
                   movie.getOverview(),
                   movie.getReleaseDate(),
                   movie.getPosterPath(),
                   movie.getBackdropPath(),
                   movie.getTitle(),
                   movie.getVoteAverage()))
                   .subscribeWith(new DisposableCompletableObserver() {
                       @Override
                       public void onComplete() {

                       }

                       @Override
                       public void onError(Throwable e) {
                            AppLogger.e(e.getMessage());
                       }
                   }));
        }

    }


    private void onMoviesFetchFailed(Throwable e){
        moviesListingFragmentCallbacks.loadingFailed(e.getMessage());
    }

    private void localMoviesearchSucccess(List<MovieEntity> movies){
        //loadedMovies.setValue(movies);

        moviesListingFragmentCallbacks.showSearchResult(movies);


    }

    private void localMovieSearchFailed(String searchText){

        //performe remote search

        mCompositeDisposable.add(mDataManager.searchMovie(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMoviesSearchSuccess, this::onMoviesSearchFailed));

        //moviesListingFragmentCallbacks.loadingFailed(e.getMessage());



    }

    private void onMoviesSearchSuccess(List<Movie> movies){

        //loadedMovies.setValue(movies);
        if(movies.size() > 0){
           // moviesListingFragmentCallbacks.showSearchResult(movies);
        }

    }

    private void onMoviesSearchFailed(Throwable e){
        moviesListingFragmentCallbacks.loadingFailed(e.getMessage());
    }

    public LiveData<List<MovieEntity>> getListMovies(){

        return mDataManager.getAllMovies();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }
}
