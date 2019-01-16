package com.hawksteam.movies.ui.movieslisting.movieslistingfragment;

import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.db.MovieEntity;

import java.util.List;

public interface MoviesListingFragmentCallbacks {

    void showMovies(List<Movie> movies);
    void loadingStarted();
    void loadingFailed(String errorMessage);
    void onMovieClicked(String id);
    void showSearchResult(List<MovieEntity> movieEntityList);

}
