package com.hawksteam.movies;

import android.app.Activity;
import android.app.Application;

import com.hawksteam.movies.data.remote.ApiClient;
import com.hawksteam.movies.data.remote.ApiService;
import com.hawksteam.movies.di.component.DaggerApplicationComponent;
import com.hawksteam.movies.utils.AppLogger;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MoviesApp extends Application implements HasActivityInjector {

    //public static ApiService apiService;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //dagger init
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this);    

        //logging init
        AppLogger.init();

    }

}
