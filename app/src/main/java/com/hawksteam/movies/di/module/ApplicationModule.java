package com.hawksteam.movies.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.hawksteam.movies.data.AppDataManager;
import com.hawksteam.movies.data.DataManager;
import com.hawksteam.movies.data.local.db.AppDatabase;
import com.hawksteam.movies.data.local.db.AppDbHelper;
import com.hawksteam.movies.data.local.db.DbHelper;
import com.hawksteam.movies.data.local.prefs.AppPreferencesHelper;
import com.hawksteam.movies.data.local.prefs.PreferencesHelper;
import com.hawksteam.movies.data.remote.ApiHelper;
import com.hawksteam.movies.data.remote.ApiService;
import com.hawksteam.movies.data.remote.AppApiHelper;
import com.hawksteam.movies.di.ApplicationContext;
import com.hawksteam.movies.di.DatabaseInfo;
import com.hawksteam.movies.di.PreferenceInfo;
import com.hawksteam.movies.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    /*private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }*/

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, @ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }

    /*@Provides
    Application provideApplication() {
        return mApplication;
    }*/

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    /*@Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 1;
    }*/

    @Provides
    SharedPreferences provideSharedPrefs(Application application) {
        return application.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }



}
