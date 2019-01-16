package com.hawksteam.movies.di.builder;


import com.hawksteam.movies.ui.moviesdetails.MoviesDetailsActivity;
import com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag.MoviesDetailsFragment;
import com.hawksteam.movies.ui.movieslisting.MoviesListingActivity;
import com.hawksteam.movies.ui.movieslisting.MoviesListingActivityModule;
import com.hawksteam.movies.ui.movieslisting.movieslistingfragment.MoviesListingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector//(modules = MoviesListingActivityModule.class)
    abstract MoviesListingActivity bindMoviesListingActivity();

    @ContributesAndroidInjector//(modules = MoviesListFragModule.class)
    abstract MoviesListingFragment bindMoviesListFragModule();

    @ContributesAndroidInjector
    abstract MoviesDetailsActivity bindMoviesDetailsActivity();

    @ContributesAndroidInjector
    abstract MoviesDetailsFragment bindMoviesDetailsFragment();

}
