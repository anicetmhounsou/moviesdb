package com.hawksteam.movies.ui.moviesdetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.hawksteam.movies.R;
import com.hawksteam.movies.data.model.api.Movie;
import com.hawksteam.movies.ui.moviesdetails.moviesdetailsfrag.MoviesDetailsFragment;
import com.hawksteam.movies.utils.AppConstants;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MoviesDetailsActivity extends AppCompatActivity
        implements MoviesDetailsFragment.OnMoviesDetailsFragInteractionListener,
        HasSupportFragmentInjector {

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
        setContentView(R.layout.activity_movies_details);

        System.err.println("language "+ Locale.getDefault().getLanguage());

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(AppConstants.IDENTIFIER))
            {
                String id = extras.getString(AppConstants.IDENTIFIER);

                if (id != null)
                {
                    System.err.println("movie recieved activity "+ id);
                    MoviesDetailsFragment movieDetailsFragment = MoviesDetailsFragment.newInstance(id);
                    getSupportFragmentManager().beginTransaction().add(R.id.movie_details_container, movieDetailsFragment).commit();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
