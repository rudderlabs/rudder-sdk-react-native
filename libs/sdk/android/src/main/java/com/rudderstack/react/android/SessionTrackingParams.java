package com.rudderstack.react.android;

import com.rudderstack.android.sdk.core.util.Utils;

class SessionTrackingParams {
    private boolean isAutomaticSessionTrackingStatus;
    private boolean isManualSessionTrackingStatus;
    private long lastEventTimeStamp;
    private static RNPreferenceManager preferenceManager;

    SessionTrackingParams() {
        preferenceManager = RNPreferenceManager.getInstance();
        refreshSessionTrackingParams();
        this.lastEventTimeStamp = preferenceManager.getLastEventTimeStamp();
    }

    void refreshSessionTrackingParams() {
        this.isAutomaticSessionTrackingStatus = preferenceManager.getAutomaticSessionTrackingStatus();
        this.isManualSessionTrackingStatus = preferenceManager.getManualSessionTrackingStatus();
    }

    /**
     * This checks whether the automatic session tracking is enabled or not
     */
    boolean isAutomaticSessionActive() {
        return this.isAutomaticSessionTrackingStatus;
    }

    boolean wasManualSessionActive() {
        return this.isManualSessionTrackingStatus;
    }

    boolean wasSessionTrackingDisabled() {
        return !this.isAutomaticSessionTrackingStatus && !this.isManualSessionTrackingStatus;
    }

    void saveEventTimestamp() {
        this.lastEventTimeStamp = Utils.getCurrentTimeInMilliSeconds();
        preferenceManager.saveLastEventTimeStamp(this.lastEventTimeStamp);
    }

    long getLastEventTimeStamp() {
        return this.lastEventTimeStamp;
    }

    void enableSessionParams(boolean automatic, boolean manual) {
        preferenceManager.saveAutomaticSessionTrackingStatus(automatic);
        preferenceManager.saveManualSessionTrackingStatus(manual);
        this.refreshSessionTrackingParams();
    }
}
