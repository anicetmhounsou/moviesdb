package com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag;

import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.api.Review;
import com.hawksteam.movies.data.model.api.Video;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;

import java.util.List;

public interface MoviesDetailsFragCallbacks {
    void showDetails(MovieEntity movie);
    void showTrailers(List<VideoEntity> trailers);
    void showReviews(List<ReviewEntity> reviews);
    void showFavorited();
    void showUnFavorited();
}
