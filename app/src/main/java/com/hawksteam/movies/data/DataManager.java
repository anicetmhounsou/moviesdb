package com.hawksteam.movies.data;

import com.hawksteam.movies.data.local.db.DbHelper;
import com.hawksteam.movies.data.local.prefs.PreferencesHelper;
import com.hawksteam.movies.data.remote.ApiHelper;

import javax.inject.Singleton;


public interface DataManager extends PreferencesHelper, ApiHelper, DbHelper {


}
