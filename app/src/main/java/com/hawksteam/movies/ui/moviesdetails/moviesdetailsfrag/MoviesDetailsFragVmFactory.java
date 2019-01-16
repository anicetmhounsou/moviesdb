package com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import io.reactivex.annotations.NonNull;


public class MoviesDetailsFragVmFactory extends ViewModelProvider.NewInstanceFactory {

    private MoviesDetailsFragCallbacks moviesDetailsFragmentCallbacks;

    public MoviesDetailsFragVmFactory (MoviesDetailsFragCallbacks moviesDetailsFragmentCallbacks) {
        this.moviesDetailsFragmentCallbacks = moviesDetailsFragmentCallbacks;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MoviesDetailsFragViewModel(moviesDetailsFragmentCallbacks);
    }

}