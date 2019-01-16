package com.hawksteam.movies.ui.movieslisting;

import android.arch.lifecycle.ViewModel;

import com.hawksteam.movies.ui.movieslisting.movieslistingfragment.MoviesListingFragment;

public class MoviesListingViewModel extends ViewModel{

    private MoviesListingFragment.OnMoviesListingFragInteractionListener movieListingFragCallbacks;

    public MoviesListingViewModel(MoviesListingFragment.OnMoviesListingFragInteractionListener movieListingFragCallbacks){
        this.movieListingFragCallbacks = movieListingFragCallbacks;
    }


}
