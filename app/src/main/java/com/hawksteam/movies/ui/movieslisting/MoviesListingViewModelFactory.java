package com.hawksteam.movies.ui.movieslisting;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.hawksteam.movies.ui.movieslisting.movieslistingfragment.MoviesListingFragment;

import io.reactivex.annotations.NonNull;

public class MoviesListingViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private MoviesListingFragment.OnMoviesListingFragInteractionListener movieListingFragCallbacks;

    public MoviesListingViewModelFactory(MoviesListingFragment.OnMoviesListingFragInteractionListener movieListingFragCallbacks){
        this.movieListingFragCallbacks = movieListingFragCallbacks;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MoviesListingViewModel(movieListingFragCallbacks);
    }

}

