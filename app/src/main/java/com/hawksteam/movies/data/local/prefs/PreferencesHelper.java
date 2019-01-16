package com.hawksteam.movies.data.local.prefs;

import com.hawksteam.movies.ui.movieslisting.sorting.SortType;

public interface PreferencesHelper {

    void setSelectedSortOption(SortType sortType);
    int getSelectedSortOption();
}
