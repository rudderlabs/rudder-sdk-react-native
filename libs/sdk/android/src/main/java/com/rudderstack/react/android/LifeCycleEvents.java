package com.rudderstack.react.android;

import android.app.Application;

import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderProperty;

import java.util.ArrayList;
import java.util.List;

public class LifeCycleEvents {

    interface LifeCycleEventsInterface {
        void run();
    }

    private static final List<LifeCycleEventsInterface> lifeCycleEvents = new ArrayList<>();

    static void trackAutomaticEvents() {
        for (LifeCycleEventsInterface lifeCycleEvents : LifeCycleEvents.lifeCycleEvents) {
            lifeCycleEvents.run();
        }
    }

    static class ApplicationStatusRunnable implements LifeCycleEventsInterface {
        private static AppVersion appVersion;
        public static final String VERSION = "version";

        ApplicationStatusRunnable(Application application) {
            appVersion = new AppVersion(application);
        }

        @Override
        public void run() {
            appVersion.storeCurrentBuildAndVersion();
            if (RNRudderSdkModule.configParams.trackLifeCycleEvents) {
                if (appVersion.previousBuild == -1) {
                    RNRudderSdkModule.userSessionPlugin.saveEventTimestamp();
                    // application was not installed previously, now triggering Application Installed event
                    sendApplicationInstalled(appVersion.currentBuild, appVersion.currentVersion);
                } else if (isApplicationUpdated()) {
                    RNRudderSdkModule.userSessionPlugin.saveEventTimestamp();
                    sendApplicationUpdated(appVersion.previousBuild, appVersion.currentBuild, appVersion.previousVersion, appVersion.currentVersion);
                }
            }
        }

        private boolean isApplicationUpdated() {
            return appVersion.previousBuild != -1 && (appVersion.previousBuild != appVersion.currentBuild);
        }

        private void sendApplicationInstalled(int currentBuild, String currentVersion) {
            RudderLogger.logDebug("LifeCycleEvents: sendApplicationInstalled: Tracking Application Installed");
            RudderProperty property = new RudderProperty()
                    .putValue(VERSION, currentVersion)
                    .putValue("build", currentBuild);
            RNRudderSdkModule.rudderClient.track("Application Installed", property);
        }

        private void sendApplicationUpdated(int previousBuild, int currentBuild, String previousVersion, String currentVersion) {
            RudderLogger.logDebug("LifeCycleEvents: sendApplicationUpdated: Tracking Application Updated");
            RudderProperty property = new RudderProperty()
                    .putValue("previous_version", previousVersion)
                    .putValue(VERSION, currentVersion)
                    .putValue("previous_build", previousBuild)
                    .putValue("build", currentBuild);
            RNRudderSdkModule.rudderClient.track("Application Updated", property);
        }
    }

    static class ApplicationOpenedRunnable implements LifeCycleEventsInterface {
        boolean fromBackground;

        ApplicationOpenedRunnable(boolean fromBackground) {
            this.fromBackground = fromBackground;
        }

        @Override
        public void run() {
            if (RNRudderSdkModule.configParams.trackLifeCycleEvents) {
                if (this.fromBackground) {
                    RNRudderSdkModule.userSessionPlugin.startNewSessionIfCurrentIsExpired();
                }
                RNRudderSdkModule.userSessionPlugin.saveEventTimestamp();
                RudderProperty property = new RudderProperty();
                property.put("from_background", this.fromBackground);
                RNRudderSdkModule.rudderClient.track("Application Opened", property);
            }
        }
    }

    static class ApplicationBackgroundedRunnable implements LifeCycleEventsInterface {
        @Override
        public void run() {
            if (RNRudderSdkModule.configParams.trackLifeCycleEvents) {
                RNRudderSdkModule.userSessionPlugin.saveEventTimestamp();
                RNRudderSdkModule.rudderClient.track("Application Backgrounded");
            }
        }
    }

    static class ScreenViewRunnable implements LifeCycleEventsInterface {
        String activityName;

        ScreenViewRunnable(String activityName) {
            this.activityName = activityName;
        }

        @Override
        public void run() {
            if (RNRudderSdkModule.configParams.recordScreenViews) {
                RNRudderSdkModule.userSessionPlugin.saveEventTimestamp();
                RudderProperty property = new RudderProperty();
                property.put("name", activityName);
                property.put("automatic", true);
                RNRudderSdkModule.rudderClient.screen(activityName, property);
            }
        }
    }

    static void executeRunnable(LifeCycleEventsInterface lifeCycleEvent) {
        if (RNRudderSdkModule.rudderClient == null && !RNRudderSdkModule.initialized) {
            lifeCycleEvents.add(lifeCycleEvent);
            return;
        }
        lifeCycleEvent.run();
    }
}
