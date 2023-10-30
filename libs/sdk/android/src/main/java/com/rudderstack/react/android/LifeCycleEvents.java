package com.rudderstack.react.android;

import android.app.Application;

import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderProperty;

import java.util.ArrayList;
import java.util.List;

public class LifeCycleEvents {

    interface LifeCycleEventsInterface {
        void run();
    }

    static class ApplicationStatusRunnable implements LifeCycleEventsInterface {
        private static AppVersion appVersion;
        public static final String VERSION = "version";
        private final RNUserSessionPlugin userSessionPlugin;
        private final boolean trackLifeCycleEvents;

        ApplicationStatusRunnable(Application application, RNUserSessionPlugin userSessionPlugin, boolean trackLifeCycleEvents) {
            this.userSessionPlugin = userSessionPlugin;
            this.trackLifeCycleEvents = trackLifeCycleEvents;
            appVersion = new AppVersion(application);
        }

        @Override
        public void run() {
            appVersion.storeCurrentBuildAndVersion();
            if (this.trackLifeCycleEvents) {
                if (appVersion.previousBuild == -1) {
                    this.userSessionPlugin.saveEventTimestamp();
                    // application was not installed previously, now triggering Application Installed event
                    sendApplicationInstalled(appVersion.currentBuild, appVersion.currentVersion);
                } else if (isApplicationUpdated()) {
                    this.userSessionPlugin.saveEventTimestamp();
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
            if (RudderClient.getInstance() != null) {
                RudderClient.getInstance().track("Application Installed", property);
            } else {
                RudderLogger.logError("RudderClient instance is null. Hence dropping Application Installed event.");
            }
        }

        private void sendApplicationUpdated(int previousBuild, int currentBuild, String previousVersion, String currentVersion) {
            RudderLogger.logDebug("LifeCycleEvents: sendApplicationUpdated: Tracking Application Updated");
            RudderProperty property = new RudderProperty()
                    .putValue("previous_version", previousVersion)
                    .putValue(VERSION, currentVersion)
                    .putValue("previous_build", previousBuild)
                    .putValue("build", currentBuild);
            if (RudderClient.getInstance() != null) {
                RudderClient.getInstance().track("Application Updated", property);
            } else {
                RudderLogger.logError("RudderClient instance is null. Hence dropping Application Updated event.");
            }
        }
    }

    static class ApplicationOpenedRunnable implements LifeCycleEventsInterface {
        boolean fromBackground;
        private final RNUserSessionPlugin userSessionPlugin;
        private final boolean trackLifeCycleEvents;

        ApplicationOpenedRunnable(boolean fromBackground, RNUserSessionPlugin userSessionPlugin, boolean trackLifeCycleEvents) {
            this.fromBackground = fromBackground;
            this.userSessionPlugin = userSessionPlugin;
            this.trackLifeCycleEvents = trackLifeCycleEvents;
        }

        @Override
        public void run() {
            if (this.trackLifeCycleEvents) {
                if (this.fromBackground) {
                    this.userSessionPlugin.startNewSessionIfCurrentIsExpired();
                }
                this.userSessionPlugin.saveEventTimestamp();
                RudderProperty property = new RudderProperty();
                property.put("from_background", this.fromBackground);
                if (RudderClient.getInstance() != null) {
                    RudderClient.getInstance().track("Application Opened", property);
                } else {
                    RudderLogger.logError("RudderClient instance is null. Hence dropping Application Opened event.");
                }
            }
        }
    }

    static class ApplicationBackgroundedRunnable implements LifeCycleEventsInterface {
        private final RNUserSessionPlugin userSessionPlugin;
        private final boolean trackLifeCycleEvents;
        
        ApplicationBackgroundedRunnable(RNUserSessionPlugin userSessionPlugin, boolean trackLifeCycleEvents) {
            this.userSessionPlugin = userSessionPlugin;
            this.trackLifeCycleEvents = trackLifeCycleEvents;
        }

        @Override
        public void run() {
            if (this.trackLifeCycleEvents) {
                this.userSessionPlugin.saveEventTimestamp();
                if (RudderClient.getInstance() != null) {
                    RudderClient.getInstance().track("Application Backgrounded");
                } else {
                    RudderLogger.logError("RudderClient instance is null. Hence dropping Application Backgrounded event.");
                }
            }
        }
    }

    static class ScreenViewRunnable implements LifeCycleEventsInterface {
        String activityName;
        private final RNUserSessionPlugin userSessionPlugin;
        private final boolean recordScreenViews;

        ScreenViewRunnable(String activityName, RNUserSessionPlugin userSessionPlugin, boolean recordScreenViews) {
            this.activityName = activityName;
            this.userSessionPlugin = userSessionPlugin;
            this.recordScreenViews = recordScreenViews;
        }

        @Override
        public void run() {
            if (this.recordScreenViews) {
                this.userSessionPlugin.saveEventTimestamp();
                RudderProperty property = new RudderProperty();
                property.put("name", activityName);
                property.put("automatic", true);
                if (RudderClient.getInstance() != null) {
                    RudderClient.getInstance().screen(activityName, property);
                } else {
                    RudderLogger.logError("RudderClient instance is null. Hence dropping Screen View event.");
                }
            }
        }
    }

    static void executeRunnable(LifeCycleEventsInterface lifeCycleEvent) {
        lifeCycleEvent.run();
    }
}
