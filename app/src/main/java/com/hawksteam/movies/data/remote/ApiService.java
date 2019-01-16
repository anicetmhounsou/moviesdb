package com.hawksteam.movies.data.remote;

import com.hawksteam.movies.data.model.api.MoviesResponse;
import com.hawksteam.movies.data.model.api.ReviewsResponse;
import com.hawksteam.movies.data.model.api.VideoResponse;

import java.util.Locale;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("3/discover/movie?sort_by=popularity.desc")
    Observable<MoviesResponse> popularMovies(@Query("page") int page, @Query("language") String lang);

    @GET("3/discover/movie?vote_count.gte=500&sort_by=vote_average.desc")
    Observable<MoviesResponse> highestRatedMovies(@Query("page") int page, @Query("language") String lang);

    @GET("3/discover/movie?sort_by=release_date.desc")
    Observable<MoviesResponse> newestMovies(@Query("release_date.lte") String maxReleaseDate,@Query("vote_count.gte") int minVoteCount,
                                            @Query("language") String lang);

    @GET("3/movie/{movieId}/videos")
    Observable<VideoResponse> trailers(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewsResponse> reviews(@Path("movieId") String movieId);

    @GET("3/search/movie?language=en-US&page=1")
    Observable<MoviesResponse> searchMovies(@Query("query") String searchQuery);

}
