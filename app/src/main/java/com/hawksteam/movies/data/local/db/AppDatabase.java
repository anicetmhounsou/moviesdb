package com.hawksteam.movies.data.local.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hawksteam.movies.data.local.db.dao.MoviesDao;
import com.hawksteam.movies.data.local.db.dao.ReviewDao;
import com.hawksteam.movies.data.local.db.dao.VideoDao;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;
import com.hawksteam.movies.di.DatabaseInfo;

@Database(entities = {MovieEntity.class, VideoEntity.class, ReviewEntity.class},
        version = 1,
        exportSchema = false )
public abstract class AppDatabase extends RoomDatabase{

    public abstract MoviesDao moviesDao();

    public abstract ReviewDao reviewDao();

    public abstract VideoDao videoDao();

}
