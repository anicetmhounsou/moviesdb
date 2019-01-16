package com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.hawksteam.movies.data.DataManager;
import com.hawksteam.movies.data.model.api.Review;
import com.hawksteam.movies.data.model.api.Video;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;
import com.hawksteam.movies.utils.AppLogger;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class MoviesDetailsFragViewModel extends ViewModel {

    private DataManager mDataManager;

    private CompositeDisposable mCompositeDisposable;

    private MoviesDetailsFragCallbacks moviesDetailsFragCallbacks;

    private MutableLiveData<MovieEntity> movieEntity = new MutableLiveData<>();

    private String id;

    public MoviesDetailsFragViewModel(MoviesDetailsFragCallbacks moviesDetailsFragCallbacks) {

        this.moviesDetailsFragCallbacks = moviesDetailsFragCallbacks;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void setDataManager(DataManager dataManager) {
        System.err.println("fragment set manager" + dataManager);
        this.mDataManager = dataManager;

    }

    public LiveData<MovieEntity> showDetails(String identifier) {

        this.id = identifier;

        return mDataManager.getMovie(identifier);

    }

    public LiveData<List<VideoEntity>> showTrailers(MovieEntity movie) {

        System.err.println("fragment data manager" + mDataManager);

        mCompositeDisposable.add(mDataManager.getTrailers(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetTrailersSuccess, t -> onGetTrailersFailure()));

        return mDataManager.getAllVideos(id);

    }



    private void onGetTrailersSuccess(List<Video> videos) {

        for (Video video : videos) {
            AppLogger.e("Insert Trailers : " + video.getName());
            mCompositeDisposable.add(mDataManager.insertVideo(new VideoEntity(video.getId(),
                    video.getName(),
                    video.getSite(),
                    video.getVideoId(),
                    video.getSize(),
                    video.getType(),
                    this.id)).subscribeWith(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable e) {
                    AppLogger.e(e.getMessage());
                }
            }));
        }

    }

    private void onGetTrailersFailure() {
        // Do nothing
    }


    public LiveData<List<ReviewEntity>> showReviews(MovieEntity movie) {

        mCompositeDisposable.add(mDataManager.getReviews(movie.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetReviewsSuccess, t -> onGetReviewsFailure()));

        return mDataManager.getAllReviews(id);

    }

    private void onGetReviewsSuccess(List<Review> reviews) {

        for (Review review : reviews) {
            AppLogger.e("Insert Reviews : " + review.getAuthor());
            mCompositeDisposable.add(mDataManager.insertReview(new ReviewEntity(review.getId(),
                    review.getAuthor(),
                    review.getContent(),
                    review.getUrl(),
                    this.id)).subscribeWith(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable e) {
                    AppLogger.e(e.getMessage());
                }
            }));
        }

    }

    private void onGetReviewsFailure() {
        // Do nothing
    }

    public void showFavoriteButton(String id) {
       /* boolean isFavorite = favoritesInteractor.isFavorite(movie.getId());

            if (isFavorite) {
                moviesDetailsFragCallbacks.showFavorited();
            } else {
                moviesDetailsFragCallbacks.showUnFavorited();
            }
       */
    }

    public void onFavoriteClick(String id) {
        /*
            boolean isFavorite = favoritesInteractor.isFavorite(movie.getId());
            if (isFavorite) {
                favoritesInteractor.unFavorite(movie.getId());
                moviesDetailsFragCallbacks.showUnFavorited();
            } else {
                favoritesInteractor.setFavorite(movie);
                moviesDetailsFragCallbacks.showFavorited();
            }
        */
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }
}
