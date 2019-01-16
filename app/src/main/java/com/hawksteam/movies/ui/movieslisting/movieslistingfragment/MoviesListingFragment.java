package com.hawksteam.movies.ui.movieslisting.movieslistingfragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hawksteam.movies.R;
import com.hawksteam.movies.data.DataManager;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.databinding.FragmentMoviesListingBinding;
import com.hawksteam.movies.utils.AppConstants;
import com.hawksteam.movies.utils.AppLogger;
import com.hawksteam.movies.utils.uiUtils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesListingFragment.OnMoviesListingFragInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MoviesListingFragment extends Fragment implements MoviesListingFragmentCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMoviesListingBinding binding;

    private MoviesListingAdapter adapter;

    private List<MovieEntity> movies = new ArrayList<>(20);

    private OnMoviesListingFragInteractionListener mListener;

    @Inject
    DataManager mDataManager;

    public MoviesListingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesListingFragment newInstance(String param1, String param2) {
        MoviesListingFragment fragment = new MoviesListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        setRetainInstance(true); //utilité à revoir car j'utilise le viewmodel
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_listing, container, false);

        View view = binding.getRoot();

        initLayoutReferences();

        binding.moviesListing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    binding.getViewModel().nextPage();
                }
            }
        });

        return view;
    }

    //voir movies d'ic aussi avec live data
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // à revoir, commenté à cause d'une erreur
        //binding.getViewModel().setView();
        if (savedInstanceState != null) {
            //à corriger et decommenter
            //movies = savedInstanceState.getParcelableArrayList(AppConstants.MOVIE);
            //adapter.notifyDataSetChanged();
            binding.moviesListing.setVisibility(View.VISIBLE);
        } else {
            //à revoir, commenté à cause d'une erreur
            //binding.getViewModel().firstPage();
        }
    }

    private void initLayoutReferences() {

        binding.moviesListing.setHasFixedSize(true);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 2;
        } else {
            columns = getResources().getInteger(R.integer.no_of_columns);
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);

        binding.moviesListing.setLayoutManager(layoutManager);

        adapter = new MoviesListingAdapter(this);

        binding.moviesListing.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.setViewModel(ViewModelProviders.of(this,
                new MoviesListingFragVmFactory(this))
                .get(MoviesListingFragmentViewModel.class));

        binding.getViewModel().setDataManager(mDataManager);


        binding.getViewModel().setView();

        subscribeMoviesLiveData();

        if (savedInstanceState != null) {
            //à corriger et décommenter
            //movies = savedInstanceState.getParcelableArrayList(AppConstants.MOVIE);
            adapter.notifyDataSetChanged();
            binding.moviesListing.setVisibility(View.VISIBLE);
        } else {
            binding.moviesListing.setVisibility(View.VISIBLE);
            binding.getViewModel().firstPage();
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                binding.getViewModel().firstPage();
                displaySortingOptions();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySortingOptions() {
        //DialogFragment sortingDialogFragment = SortingDialogFragment.newInstance(moviesPresenter);
        //sortingDialogFragment.show(getFragmentManager(), "Select Quantity");
    }


    @Override
    public void onAttach(Context context) {

        AndroidSupportInjection.inject(this);

        super.onAttach(context);
        if (context instanceof OnMoviesListingFragInteractionListener) {
            mListener = (OnMoviesListingFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMoviesDetailsFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }


    //revoir l'affaire de movies ici avec mutable live data
    @Override
    public void showMovies(List<Movie> movies) {
        //this.movies.clear();
        //this.movies.addAll(movies);
        binding.moviesListing.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();


        //à ajouter plus tard
        //mListener.onMoviesLoaded(movies.get(0));
    }

    @Override
    public void loadingStarted() {
        Snackbar.make(binding.moviesListing, R.string.loading_movies, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String errorMessage) {
        Snackbar.make(binding.moviesListing, errorMessage, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void onMovieClicked(String id) {
        mListener.onMovieClicked(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConstants.MOVIE, (ArrayList) movies);
    }

    private void subscribeMoviesLiveData() {
        binding.getViewModel().getListMovies().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> movies) {
                System.err.println("livedata subscription movies"+ movies.size());
                adapter.setMoviesList(movies);
            }
        });
    }

    @Override
    public void showSearchResult(List<MovieEntity> movieEntityList) {
        System.err.println("search result list"+ movies.size());
        adapter.setMoviesList(movies);
    }

    public void searchViewClicked(String searchText) {
        binding.getViewModel().searchMovie(searchText);
    }

    public void searchViewBackButtonClicked() {
        binding.getViewModel().searchMovieBackPressed();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public interface OnMoviesListingFragInteractionListener {

        void onMoviesLoaded(String id);

        void onMovieClicked(String id);

    }
}
