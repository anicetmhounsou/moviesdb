package com.hawksteam.movies.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hawksteam.movies.data.model.db.MovieEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieEntity>> getAllMovies();

    @Query("SELECT * FROM movie WHERE id = :identifier LIMIT 1")
    LiveData<MovieEntity> getMovie(String identifier);

    @Query("SELECT * FROM movie WHERE title LIKE :query")
    Flowable<List<MovieEntity>> searchMovie(String query);

    @Query("SELECT * FROM movie WHERE id LIKE :identifier LIMIT 1")
    MovieEntity getMovieSimple(String identifier);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntity movieEntity);

    @Update
    void updateVideo(MovieEntity movieEntity);

}
