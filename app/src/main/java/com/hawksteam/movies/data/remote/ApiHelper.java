package com.hawksteam.movies.data.remote;


import android.support.annotation.NonNull;

import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.api.Review;
import com.hawksteam.movies.data.model.api.Video;

import java.util.List;

import io.reactivex.Observable;

public interface ApiHelper {

    Observable<List<Movie>> fetchMovies(int page, int selectedOption);

    Observable<List<Movie>> searchMovie(@NonNull String searchQuery);

    boolean isPaginationSupported(int selectedOption);

    Observable<List<Video>> getTrailers(String id);
    Observable<List<Review>> getReviews(String id);
}
