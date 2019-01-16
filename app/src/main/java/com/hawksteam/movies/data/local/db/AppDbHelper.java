package com.hawksteam.movies.data.local.db;


import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppDbHelper implements DbHelper {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDbHelper(AppDatabase appDatabase){
        this.mAppDatabase = appDatabase;
    }


    @Override
    public LiveData<List<MovieEntity>> getAllMovies() {
        return mAppDatabase.moviesDao().getAllMovies();
    }

    @Override
    public Flowable<List<MovieEntity>> searchLocalMovie(String query) {
        return mAppDatabase.moviesDao().searchMovie(query);
    }

    @Override
    public LiveData<MovieEntity> getMovie(String identifier) {
        return mAppDatabase.moviesDao().getMovie(identifier);
    }

    @Override
    public Completable getMovieSimple(String identifier) {
        return getMovieAsynch(identifier);
    }

    @Override
    public Completable insertMovie(MovieEntity movieEntity) {
        return insertAsynchMovie(movieEntity);
    }

    @Override
    public LiveData<List<VideoEntity>> getAllVideos(String id) {
        return mAppDatabase.videoDao().getAllVideo(id);
    }

    @Override
    public Completable insertVideo(VideoEntity videoEntity) {
        return insertAsynchTrailer(videoEntity);
    }

    @Override
    public LiveData<List<ReviewEntity>> getAllReviews(String id) {

        return mAppDatabase.reviewDao().getAllReviews(id);
    }

    @Override
    public Completable insertReview(ReviewEntity reviewEntity) {
        return insertAsynchReview(reviewEntity);
    }

    private Completable insertAsynchMovie(MovieEntity movieEntity){

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.moviesDao().insert(movieEntity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private Completable insertAsynchTrailer(VideoEntity videoEntity){
     return Completable.fromAction(new Action() {
         @Override
         public void run() throws Exception {
            mAppDatabase.videoDao().insert(videoEntity);
         }
     }).subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable insertAsynchReview(ReviewEntity reviewEntity){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.reviewDao().insert(reviewEntity);
            }
        });
    }

    private Completable getMovieAsynch(String identifier){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.moviesDao().getMovieSimple(identifier);
            }
        });
    }

     

}
