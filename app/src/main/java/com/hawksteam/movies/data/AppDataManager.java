package com.hawksteam.movies.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hawksteam.movies.data.local.db.DbHelper;
import com.hawksteam.movies.data.local.prefs.PreferencesHelper;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.api.Review;
import com.hawksteam.movies.data.model.api.Video;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;
import com.hawksteam.movies.data.remote.ApiHelper;
import com.hawksteam.movies.ui.movieslisting.sorting.SortType;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
public class AppDataManager implements DataManager {

    private final ApiHelper mApiHelper;

    private final DbHelper mDbHelper;

    private final PreferencesHelper mPreferencesHelper;

    public static String test= "teststring";

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper, ApiHelper apiHelper, DbHelper dbHelper) {
        //mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mDbHelper = dbHelper;
    }

    @Override
    public Observable<List<Movie>> fetchMovies(int page, int selectedOption) {

        return mApiHelper.fetchMovies(page, mPreferencesHelper.getSelectedSortOption());

    }

    @Override
    public Flowable<List<MovieEntity>> searchLocalMovie(String query) {
        return mDbHelper.searchLocalMovie(query);
    }

    @Override
    public Observable<List<Movie>> searchMovie(@NonNull String searchQuery) {
        return mApiHelper.searchMovie(searchQuery);
    }

    @Override
    public boolean isPaginationSupported(int selectedOption) {
        return mApiHelper.isPaginationSupported(mPreferencesHelper.getSelectedSortOption());
    }

    @Override
    public Observable<List<Video>> getTrailers(String id) {
        return mApiHelper.getTrailers(id);
    }

    @Override
    public Observable<List<Review>> getReviews(String id) {
        return mApiHelper.getReviews(id);
    }

    @Override
    public int getSelectedSortOption() {
        return mPreferencesHelper.getSelectedSortOption();
    }

    @Override
    public void setSelectedSortOption(SortType sortType) {
        mPreferencesHelper.setSelectedSortOption(sortType);
    }


    @Override
    public LiveData<List<MovieEntity>> getAllMovies() {
        return mDbHelper.getAllMovies();
    }

    @Override
    public LiveData<MovieEntity> getMovie(String identifier) {
        return mDbHelper.getMovie(identifier);
    }

    @Override
    public Completable getMovieSimple(String identifier) {
        return mDbHelper.getMovieSimple(identifier);
    }

    @Override
    public Completable insertMovie(MovieEntity movieEntity) {
        return mDbHelper.insertMovie(movieEntity);
    }

    @Override
    public LiveData<List<VideoEntity>> getAllVideos(String id) {
        return mDbHelper.getAllVideos(id);
    }

    @Override
    public Completable insertVideo(VideoEntity videoEntity)  {
        return mDbHelper.insertVideo(videoEntity);
    }

    @Override
    public LiveData<List<ReviewEntity>> getAllReviews(String id) {
        return mDbHelper.getAllReviews(id);
    }

    @Override
    public Completable insertReview(ReviewEntity reviewEntity) {
        return mDbHelper.insertReview(reviewEntity);
    }
}
