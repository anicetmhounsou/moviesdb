package com.hawksteam.movies.data.remote;

import android.support.annotation.NonNull;

import com.hawksteam.movies.MoviesApp;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.api.MoviesResponse;
import com.hawksteam.movies.data.model.api.Review;
import com.hawksteam.movies.data.model.api.ReviewsResponse;
import com.hawksteam.movies.data.model.api.Video;
import com.hawksteam.movies.data.model.api.VideoResponse;
import com.hawksteam.movies.ui.movieslisting.sorting.SortType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppApiHelper implements ApiHelper {

    private static final int NEWEST_MIN_VOTE_COUNT = 50;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final ApiService apiService;
    private String lang = Locale.getDefault().getLanguage();

    @Inject
    public AppApiHelper(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<List<Movie>> fetchMovies(int page, int selectedOption) {

        if (selectedOption == SortType.MOST_POPULAR.getValue()) {

            return apiService.popularMovies(page, lang).map(MoviesResponse::getResults);

        } else if (selectedOption == SortType.HIGHEST_RATED.getValue()) {

            return apiService.highestRatedMovies(page, lang).map(MoviesResponse::getResults);

        } else if (selectedOption == SortType.NEWEST.getValue()) {

            Calendar cal = Calendar.getInstance();
            String maxReleaseDate = dateFormat.format(cal.getTime());
            return apiService.newestMovies(maxReleaseDate, NEWEST_MIN_VOTE_COUNT, lang).map(MoviesResponse::getResults);

        }else{

            //à changer quand l'option facori sera entrée
            return apiService.highestRatedMovies(page, lang).map(MoviesResponse::getResults);

        }

    }

    @Override
    public Observable<List<Movie>> searchMovie(@NonNull String searchQuery) {
        return apiService.searchMovies(searchQuery).map(MoviesResponse::getResults);
    }

    @Override
    public boolean isPaginationSupported(int selectedOption) {

        return selectedOption != SortType.FAVORITES.getValue();
    }

    public Observable<List<Video>> getTrailers(final String id) {
        return apiService.trailers(id).map(VideoResponse::getVideos);
    }


    public Observable<List<Review>> getReviews(final String id) {
        return apiService.reviews(id).map(ReviewsResponse::getReviews);
    }
}
