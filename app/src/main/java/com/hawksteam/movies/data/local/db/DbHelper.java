package com.hawksteam.movies.data.local.db;

import android.arch.lifecycle.LiveData;

import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DbHelper {

    LiveData<List<MovieEntity>> getAllMovies();

    LiveData<MovieEntity> getMovie(String identifier);

    Completable getMovieSimple(String identifier);

    Completable insertMovie(final MovieEntity movieEntity);

    LiveData<List<VideoEntity>> getAllVideos(String id);

    Flowable<List<MovieEntity>> searchLocalMovie(String query);

    Completable insertVideo(final VideoEntity videoEntity);

    LiveData<List<ReviewEntity>> getAllReviews(String identifier);

    Completable insertReview(final ReviewEntity reviewEntity);
}
