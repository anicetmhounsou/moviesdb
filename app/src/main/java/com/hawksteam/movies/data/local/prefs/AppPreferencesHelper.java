package com.hawksteam.movies.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.hawksteam.movies.di.PreferenceInfo;
import com.hawksteam.movies.ui.movieslisting.sorting.SortType;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_SORT_SELECT_OPTION = "PREF_SORT_SELECT_OPTION";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(SharedPreferences sharedPreferences){
        this.mPrefs = sharedPreferences;
    }

    @Override
    public void setSelectedSortOption(SortType sortType) {
        mPrefs.edit().putInt(PREF_SORT_SELECT_OPTION, sortType.getValue()).apply();
    }

    @Override
    public int getSelectedSortOption() {
        return mPrefs.getInt(PREF_SORT_SELECT_OPTION, 0);
    }
}
