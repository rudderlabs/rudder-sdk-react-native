package com.rudderstack.react.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.rudderstack.android.sdk.core.RudderLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Preference Manager for React Native Android SDK
 */
public class RNPreferenceManager {
    private static final String NATIVE_PREFS_NAME = "rl_prefs";
    private static final String REACT_NATIVE_PREFS_NAME = "rn_prefs";
    private final Application application;
    private static final String RUDDER_LAST_EVENT_TIMESTAMP_KEY = "rudder_last_event_timestamp_key";
    private static final String RUDDER_SESSION_AUTO_TRACKING_STATUS_KEY = "rudder_session_auto_tracking_status_key";
    private static final String RUDDER_SESSION_MANUAL_TRACKING_STATUS_KEY = "rudder_session_manual_tracking_status_key";

    private static final String RUDDER_APPLICATION_BUILD_KEY = "rl_application_build_key";
    private static final String RUDDER_APPLICATION_VERSION_KEY = "rl_application_version_key";

    private static SharedPreferences preferences;
    private static RNPreferenceManager instance;

    private RNPreferenceManager(Application application) {
        this.application = application;
        preferences = application.getSharedPreferences(REACT_NATIVE_PREFS_NAME, 0);
    }

    @Nonnull
    static RNPreferenceManager getInstance(Application application) {
        if (instance == null) {
            instance = new RNPreferenceManager(application);
        }
        return instance;
    }

    @Nullable
    static RNPreferenceManager getInstance() {
        return instance;
    }

    // Lifecycle methods

    int getBuildNumber() {
        return preferences.getInt(RUDDER_APPLICATION_BUILD_KEY, -1);
    }

    void saveBuildNumber(int versionCode) {
        preferences.edit().putInt(RUDDER_APPLICATION_BUILD_KEY, versionCode).apply();
    }

    String getVersionName() {
        return preferences.getString(RUDDER_APPLICATION_VERSION_KEY, null);
    }

    void saveVersionName(String versionName) {
        preferences.edit().putString(RUDDER_APPLICATION_VERSION_KEY, versionName).apply();
    }

    // Session Tracking methods

    void saveLastEventTimeStamp(Long time) {
        preferences.edit().putLong(RUDDER_LAST_EVENT_TIMESTAMP_KEY, time).apply();
    }

    Long getLastEventTimeStamp() {
        return preferences.getLong(RUDDER_LAST_EVENT_TIMESTAMP_KEY, -1);
    }

    void saveAutomaticSessionTrackingStatus(boolean status) {
        preferences.edit().putBoolean(RUDDER_SESSION_AUTO_TRACKING_STATUS_KEY, status).apply();
    }

    boolean getAutomaticSessionTrackingStatus() {
        return preferences.getBoolean(RUDDER_SESSION_AUTO_TRACKING_STATUS_KEY, false);
    }

    void saveManualSessionTrackingStatus(boolean status) {
        preferences.edit().putBoolean(RUDDER_SESSION_MANUAL_TRACKING_STATUS_KEY, status).apply();
    }

    boolean getManualSessionTrackingStatus() {
        return preferences.getBoolean(RUDDER_SESSION_MANUAL_TRACKING_STATUS_KEY, false);
    }

    public void migrateAppInfoPreferencesWhenRNPrefDoesNotExist() {
        SharedPreferences nativePrefs = this.application.getSharedPreferences(NATIVE_PREFS_NAME, Context.MODE_PRIVATE);
        if (!preferences.contains(RUDDER_APPLICATION_BUILD_KEY) &&
                nativePrefs.contains(RUDDER_APPLICATION_BUILD_KEY)) {
            saveBuildNumber(nativePrefs.getInt(RUDDER_APPLICATION_BUILD_KEY, -1));
        }
        if (!preferences.contains(RUDDER_APPLICATION_VERSION_KEY) &&
                nativePrefs.contains(RUDDER_APPLICATION_VERSION_KEY)) {
            saveVersionName(nativePrefs.getString(RUDDER_APPLICATION_VERSION_KEY, null));
        }
    }
}
