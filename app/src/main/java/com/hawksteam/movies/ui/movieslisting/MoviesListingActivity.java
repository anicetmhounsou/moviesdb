package com.hawksteam.movies.ui.movieslisting;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.hawksteam.movies.R;
import com.hawksteam.movies.data.DataManager;
import com.hawksteam.movies.data.local.db.DbHelper;
import com.hawksteam.movies.data.local.prefs.PreferencesHelper;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.data.model.db.MovieEntity;
import com.hawksteam.movies.data.remote.ApiHelper;
import com.hawksteam.movies.databinding.ActivityMoviesListingBinding;
import com.hawksteam.movies.di.ApplicationContext;
import com.hawksteam.movies.di.DatabaseInfo;
import com.hawksteam.movies.ui.moviesdetails.MoviesDetailsActivity;
import com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag.MoviesDetailsFragment;
import com.hawksteam.movies.ui.movieslisting.movieslistingfragment.MoviesListingFragment;
import com.hawksteam.movies.utils.AppConstants;
import com.hawksteam.movies.utils.AppLogger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MoviesListingActivity extends AppCompatActivity
        implements MoviesListingFragment.OnMoviesListingFragInteractionListener,
        MoviesDetailsFragment.OnMoviesDetailsFragInteractionListener,
        HasSupportFragmentInjector{

    private ActivityMoviesListingBinding binding;

    private boolean twoPaneMode;

    public static final String DETAILS_FRAGMENT = "DetailsFragment";

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies_listing);

        binding.setViewModel(ViewModelProviders.of(this,
                new MoviesListingViewModelFactory(this))
                .get(MoviesListingViewModel.class));

        setToolbar();


        if (findViewById(R.id.movie_details_container) != null) {
            twoPaneMode = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new MoviesDetailsFragment())
                        .commit();
            }

        } else {
            twoPaneMode = false;
        }

        handleIntent(getIntent());



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //perform search action
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query){

        if(query.length() > 0){
            final MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
            mlFragment.searchViewClicked(query);
        }

    }

    private void setToolbar(){
        setSupportActionBar(binding.toolbarPath.toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void startMovieDetailsActivity(String id) {
        Intent intent = new Intent(this, MoviesDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(AppConstants.IDENTIFIER, id);
        intent.putExtras(extras);
        System.err.println("movie sended "+ id);
        startActivity(intent);
    }

    private void loadMovieDetailsFragment(String id) {
        MoviesDetailsFragment movieDetailsFragment = MoviesDetailsFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean("search", true);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

         SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         /*searchManager.setOnDismissListener(new SearchManager.OnDismissListener() {
             @Override
             public void onDismiss() {
                 AppLogger.w("Search dismuss");
                 MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
                 mlFragment.searchViewBackButtonClicked();
             }
         });
         searchManager.setOnCancelListener(new SearchManager.OnCancelListener() {
             @Override
             public void onCancel() {
                 AppLogger.w("Search cancel");
                 MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
                 mlFragment.searchViewBackButtonClicked();
             }
         });*/

         SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
         searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );
         searchView.setIconifiedByDefault(false);
         searchView.setSubmitButtonEnabled(true);
         searchView.setIconifiedByDefault(false);


        /*final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        final MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
                mlFragment.searchViewBackButtonClicked();
                return true;
            }
        });

        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 0) {
                        mlFragment.searchViewClicked(charSequence.toString());
                    }
                });*/

        return true;
    }

    @Override
    public void onMoviesLoaded(String id) {
        if (twoPaneMode) {
            loadMovieDetailsFragment(id);
        } else {
            // Do not load in single pane view
        }
    }

    @Override
    public void onMovieClicked(String id) {
        System.err.println("on moviee clicked");
        if (twoPaneMode) {
            loadMovieDetailsFragment(id);
        } else {
            startMovieDetailsActivity(id);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
