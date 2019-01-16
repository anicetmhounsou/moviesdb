package com.hawksteam.movies.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hawksteam.movies.data.model.db.VideoEntity;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM video WHERE movieid = :identifier")
    LiveData<List<VideoEntity>> getAllVideo(String identifier);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoEntity videoEntity);

    @Update
    void updateVideo(VideoEntity videoEntity);

}
