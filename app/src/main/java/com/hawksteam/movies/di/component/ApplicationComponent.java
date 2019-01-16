package com.hawksteam.movies.di.component;


import android.app.Application;

import com.hawksteam.movies.MoviesApp;
import com.hawksteam.movies.di.builder.BuildersModule;
import com.hawksteam.movies.di.module.ApplicationModule;
import com.hawksteam.movies.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, ApplicationModule.class, BuildersModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(MoviesApp moviesApp);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}
