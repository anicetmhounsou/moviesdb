package com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hawksteam.movies.R;
import com.hawksteam.movies.data.DataManager;
import com.hawksteam.movies.data.model.api.Review;
import com.hawksteam.movies.data.model.api.Video;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.model.db.ReviewEntity;
import com.hawksteam.movies.data.model.db.VideoEntity;
import com.hawksteam.movies.data.remote.ApiEndPoint;
import com.hawksteam.movies.databinding.FragmentMoviesDetailsBinding;
import com.hawksteam.movies.utils.AppConstants;
import com.hawksteam.movies.utils.AppLogger;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMoviesDetailsFragInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesDetailsFragment extends Fragment implements MoviesDetailsFragCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;

    private MovieEntity movie;

    private OnMoviesDetailsFragInteractionListener mListener;

    private FragmentMoviesDetailsBinding binding;

    private String id;

    @Inject
    DataManager mDataManager;

    public MoviesDetailsFragment() {
        // Required empty public constructor
    }

    public static MoviesDetailsFragment newInstance(@NonNull String id) {
        MoviesDetailsFragment fragment = new MoviesDetailsFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.IDENTIFIER, id);
        System.err.println("movie recieved fragment "+ id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_details, container, false);

        View view = binding.getRoot();

        setToolbar();

        binding.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClick();
            }
        });


        return view;
    }

    private void setToolbar()
    {
        binding.collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        binding.collapsingToolbar.setTitle(getString(R.string.movie_details));
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        binding.collapsingToolbar.setTitleEnabled(true);

        if (binding.toolbar != null)
        {
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else
        {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.setViewModel(ViewModelProviders.of(this,
                new MoviesDetailsFragVmFactory(this))
                .get(MoviesDetailsFragViewModel.class));

        binding.getViewModel().setDataManager(mDataManager);

        System.err.println("viewmodel "+ binding.getViewModel()     );

        System.err.println("setting datamanager "+ mDataManager   );

        if (getArguments() != null)
        {
            id = (String) getArguments().get(AppConstants.IDENTIFIER);
            System.err.println("movie recieved fragment 2 "+ id);
            if (id != null)
            {
                System.err.println("movie recieved fragment 3 "+ id);

                //binding.getViewModel().setView(this);
                binding.getViewModel().showDetails(id).observe(this, new Observer<MovieEntity>() {
                    @Override
                    public void onChanged(@Nullable MovieEntity movieEntity) {
                        if(movieEntity != null){
                            AppLogger.e("Selected movie details title : "+movieEntity.getTitle());
                            showDetails(movieEntity);
                        }
                    }
                });

                binding.getViewModel().showFavoriteButton(id);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof OnMoviesDetailsFragInteractionListener) {
            mListener = (OnMoviesDetailsFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMoviesDetailsFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showDetails(MovieEntity movie) {
        Glide.with(getContext()).load(ApiEndPoint.getBackdropPath(movie.getBackdropPath())).into(binding.moviePoster);
        binding.movieName.setText(movie.getTitle());
        binding.movieYear.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
        binding.movieRating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVoteAverage())));
        binding.movieDescription.setText(movie.getOverview());

        binding.getViewModel().showTrailers(movie).observe(this, new Observer<List<VideoEntity>>() {
            @Override
            public void onChanged(@Nullable List<VideoEntity> videoEntities) {
                if(videoEntities != null){
                    showTrailers(videoEntities);
                }
            }
        });

        binding.getViewModel().showReviews(movie).observe(this, new Observer<List<ReviewEntity>>() {
            @Override
            public void onChanged(@Nullable List<ReviewEntity> reviewEntities) {
                if(reviewEntities != null){
                    showReviews(reviewEntities);
                }
            }
        });

    }

    @Override
    public void showTrailers(List<VideoEntity> trailers) {
        if (trailers.isEmpty())
        {
            /*binding.trailersReviews.trailersLabel.setVisibility(View.GONE);
            binding.trailersReviews.trailers.setVisibility(View.GONE);
            binding.trailersReviews.trailersContainer.setVisibility(View.GONE);*/

        } else
        {
            binding.trailersReviews.trailersLabel.setVisibility(View.VISIBLE);
            binding.trailersReviews.trailers.setVisibility(View.VISIBLE);
            binding.trailersReviews.trailersContainer.setVisibility(View.VISIBLE);

            binding.trailersReviews.trailers.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPrimary)
                    .centerCrop()
                    .override(150, 150);

            for (VideoEntity trailer : trailers)
            {
                View thumbContainer = inflater.inflate(R.layout.video, binding.trailersReviews.trailers, false);
                ImageView thumbView = thumbContainer.findViewById(R.id.video_thumb);
                thumbView.setTag(R.id.glide_tag, Video.getUrl(trailer));
                thumbView.requestLayout();
                thumbView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onThumbnailClick(v);
                    }
                });
                Glide.with(getContext())
                        .load(Video.getThumbnailUrl(trailer))
                        .apply(options)
                        .into(thumbView);
                binding.trailersReviews.trailers.addView(thumbContainer);
            }
        }
    }

    @Override
    public void showReviews(List<ReviewEntity> reviews) {
        if (reviews.isEmpty())
        {
            /*binding.trailersReviews.reviewsLabel.setVisibility(View.GONE);
            binding.trailersReviews.reviews.setVisibility(View.GONE);*/
        } else
        {
            binding.trailersReviews.reviewsLabel.setVisibility(View.VISIBLE);
            binding.trailersReviews.reviews.setVisibility(View.VISIBLE);

            binding.trailersReviews.reviews.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (ReviewEntity review : reviews)
            {
                ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, binding.trailersReviews.reviews, false);
                TextView reviewAuthor = reviewContainer.findViewById(R.id.review_author);
                TextView reviewContent = reviewContainer.findViewById(R.id.review_content);
                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReviewClick((TextView) v);
                    }
                });
                binding.trailersReviews.reviews.addView(reviewContainer);
            }
        }
    }

    @Override
    public void showFavorited() {
        binding.favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white_24dp));
    }

    private void onReviewClick(TextView view)
    {
        /*if (view.getMaxLines() == 5)
        {
            view.setMaxLines(500);
        } else
        {
            view.setMaxLines(5);
        }*/
    }

    private void onThumbnailClick(View view)
    {
        String videoUrl = (String) view.getTag(R.id.glide_tag);
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    private void onFavoriteClick()
    {
        binding.getViewModel().onFavoriteClick(id);
    }

    @Override
    public void showUnFavorited() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMoviesDetailsFragInteractionListener {
        // TODO: Update argument type and name

    }
}
