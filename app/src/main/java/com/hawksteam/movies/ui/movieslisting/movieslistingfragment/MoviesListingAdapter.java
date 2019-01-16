package com.hawksteam.movies.ui.movieslisting.movieslistingfragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hawksteam.movies.R;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.remote.ApiEndPoint;
import com.hawksteam.movies.databinding.MovieGridItemBinding;
import com.hawksteam.movies.utils.AppLogger;

import java.util.List;

public class MoviesListingAdapter extends RecyclerView.Adapter<MoviesListingAdapter.ViewHolder>{

    private List<MovieEntity> moviesList;
    private Context context;
    private LayoutInflater layoutInflater;
    private MoviesListingFragmentCallbacks callbacks;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final MovieGridItemBinding binding;

        public MovieEntity movie;

        public ViewHolder(final MovieGridItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

        @Override
        public void onClick(View v) {
            System.err.println("adapter movie clicked");
           // MoviesListingAdapter.this.callbacks.onMovieClicked(movie);
        }
    }

    public MoviesListingAdapter( MoviesListingFragmentCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void setMoviesList(List<MovieEntity> movies){
        this.moviesList = movies;
        AppLogger.e("movies passed : "+movies.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        if(layoutInflater ==null ){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        MovieGridItemBinding viewHolderBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.movie_grid_item, parent, false);

        return new ViewHolder(viewHolderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


       // AppLogger.e("Movies binding : "+holder.movie.getTitle());

        holder.movie = moviesList.get(position);
        holder.binding.movieName.setText(holder.movie.getTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_img_error)
                .onlyRetrieveFromCache(false)
                .priority(Priority.HIGH);

        Glide.with(context)
                .asBitmap()
                .load(ApiEndPoint.getPosterPath(holder.movie.getPosterPath()))
                .apply(options)
                .into(new BitmapImageViewTarget(holder.binding.moviePoster){
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, holder));
                    }
                });

        holder.binding.movieContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("adapter movie clicked");
                MoviesListingAdapter.this.callbacks.onMovieClicked(holder.movie.getId());
            }
        });
    }

    private void setBackgroundColor(Palette palette, ViewHolder holder) {
        holder.binding.titleBackground.setBackgroundColor(palette.getVibrantColor(context
                .getResources().getColor(R.color.black_translucent_60)));
    }

    @Override
    public int getItemCount() {

        if(moviesList != null){
            AppLogger.e("MoviesListingAdapter item count "+ moviesList.size());
            return moviesList.size();
        }else{
            return 0;
        }

    }
}
