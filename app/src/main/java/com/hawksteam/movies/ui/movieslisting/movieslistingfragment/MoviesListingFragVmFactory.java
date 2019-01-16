package com.hawksteam.movies.ui.movieslisting.movieslistingfragment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import io.reactivex.annotations.NonNull;

public class MoviesListingFragVmFactory extends ViewModelProvider.NewInstanceFactory {

    private MoviesListingFragmentCallbacks moviesListingFragmentCallbacks;

    public MoviesListingFragVmFactory (MoviesListingFragmentCallbacks moviesListingFragmentCallbacks) {
        this.moviesListingFragmentCallbacks = moviesListingFragmentCallbacks;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MoviesListingFragmentViewModel(moviesListingFragmentCallbacks);
    }

}
