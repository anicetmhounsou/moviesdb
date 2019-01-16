package com.hawksteam.movies.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hawksteam.movies.data.model.db.ReviewEntity;

import java.util.List;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM review WHERE movieid = :identifier")
    LiveData<List<ReviewEntity>> getAllReviews(String identifier);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ReviewEntity reviewEntity);

    @Update
    void updateReview(ReviewEntity reviewEntity);

}
