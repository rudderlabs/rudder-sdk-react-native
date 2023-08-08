package com.rudderstack.react.android;

import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.util.Utils;

/**
 * This class is used to manage the user session using the native manual session tracking API i.e., startSession and endSession.
 */
public class RNUserSessionPlugin {
    private final long sessionTimeOut;
    private final RudderClient rudderClient;
    private final SessionTrackingParams sessionParams;
    private final boolean isAutomaticSessionTrackingEnabled;

    RNUserSessionPlugin(
            boolean automaticSessionTrackingStatus,
            boolean lifecycleEventsTrackingStatus,
            long sessionTimeout) {
        this.sessionTimeOut = sessionTimeout;
        this.rudderClient = RudderClient.getInstance();

        this.sessionParams = new SessionTrackingParams();

        this.isAutomaticSessionTrackingEnabled = automaticSessionTrackingStatus && lifecycleEventsTrackingStatus;
    }

    /**
     * This handles the session tracking:
     * If Automatic Session Tracking is disabled in the config, then handle manual session tracking.
     * Otherwise, handle Automatic Session Tracking.
     */
    void handleSessionTracking() {
        if (!isAutomaticSessionTrackingEnabled) {
            endSessionIfManualSessionInactivePreviously();
        } else {
            handleAutomaticSessionTracking();
        }
    }

    /**
     * This handles the manual session tracking:
     * <p>
     * If previously, manual session tracking was disabled, then end the session and disable all session params.
     * Otherwise, do nothing.
     */
    private void endSessionIfManualSessionInactivePreviously() {
        if (!this.sessionParams.wasManualSessionActive()) {
            RudderLogger.logVerbose("RNUserSessionPlugin: As previously manual session tracking was not enabled. Hence clear the session");
            endSession();
        }
    }

    /**
     * This handles the automatic session tracking
     * <p>
     * Start new session::
     * <ol>
     *     <li>If previously, Manual Session Tracking was active</li>
     *     <li>If previously, Session Tracking was disabled</li>
     *     <li>If the current session is expired</li>
     * </ol>
     * Enable the automatic session params
     */
    private void handleAutomaticSessionTracking() {
        if (this.sessionParams.wasManualSessionActive() ||
                this.sessionParams.wasSessionTrackingDisabled()) {
            RudderLogger.logVerbose("RNUserSessionPlugin: As previously manual session tracking was enabled or session tracking was disabled. Hence start a new session");
            startSession();
        } else {
            startNewSessionIfCurrentIsExpired();
        }
        enableAutomaticSessionParams();
    }

    /**
     * This checks if the current session is expired or not and accordingly starts a new session
     */
    void startNewSessionIfCurrentIsExpired() {
        if (this.sessionParams.isAutomaticSessionActive()) {
            if (isSessionExpired()) {
                RudderLogger.logVerbose("RNUserSessionPlugin: previous session is expired");
                startSession();
            }
        }
    }

    /**
     * This checks if the session is expired or not
     *
     * @return true if the session is expired, false otherwise
     */
    private boolean isSessionExpired() {
        long currentTime = Utils.getCurrentTimeInMilliSeconds();
        long timeDifference;
        synchronized (this) {
            timeDifference = Math.abs(currentTime - this.sessionParams.getLastEventTimeStamp());
        }
        return timeDifference >= sessionTimeOut;
    }

    /**
     * This saves the event time
     */
    void saveEventTimestamp() {
        this.sessionParams.saveEventTimestamp();
    }

    /**
     * This starts either a new automatic or manual session
     */
    void startSession() {
        if (this.rudderClient != null) {
            this.rudderClient.startSession();
            RudderLogger.logVerbose("RNUserSessionPlugin: starting new session");
        }
    }

    /**
     * This starts a new manual session with the given session id
     *
     * @param sessionId The timestamp of the event
     */
    void startSession(long sessionId) {
        if (this.rudderClient != null) {
            this.rudderClient.startSession(sessionId);
            RudderLogger.logVerbose("RNUserSessionPlugin: starting new session with sessionId: " + sessionId);
        }
    }

    /**
     * This ends the session
     */
    void endSession() {
        if (this.rudderClient != null) {
            this.rudderClient.endSession();
            disableSessionParams();
            RudderLogger.logVerbose("RNUserSessionPlugin: ending session");
        }
    }

    /**
     * This enables the Automatic session tracking and disables the other session tracking params
     */
    private void enableAutomaticSessionParams() {
        this.sessionParams.enableSessionParams(true, false);
    }

    /**
     * This enables the Manual session tracking and disables the other session tracking params
     */
    void enableManualSessionParams() {
        this.sessionParams.enableSessionParams(false, true);
    }

    /**
     * This disable the session tracking params
     */
    private void disableSessionParams() {
        this.sessionParams.enableSessionParams(false, false);
    }
}
