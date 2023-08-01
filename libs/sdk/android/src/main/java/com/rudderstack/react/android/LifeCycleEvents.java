package com.rudderstack.react.android;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

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

    static class ApplicationInstalledOrUpdatedRunnable implements LifeCycleEventsInterface {
        private static AppVersion appVersion;
        public static final String VERSION = "version";

        ApplicationInstalledOrUpdatedRunnable(Application application) {
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

    private static class AppVersion {
        int previousBuild;
        int currentBuild;
        String previousVersion;
        String currentVersion;
        RNPreferenceManager preferenceManager;


        AppVersion(Application application) {
            try {
                this.preferenceManager = RNPreferenceManager.getInstance(application);
                previousBuild = preferenceManager.getBuildNumber();
                previousVersion = preferenceManager.getVersionName();
                RudderLogger.logDebug("Previous Installed Version: " + previousVersion);
                RudderLogger.logDebug("Previous Installed Build: " + previousBuild);
                String packageName = application.getPackageName();
                PackageManager packageManager = application.getPackageManager();
                if (packageManager == null)
                    return;
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                currentVersion = packageInfo.versionName;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    currentBuild = (int) packageInfo.getLongVersionCode();
                } else {
                    currentBuild = packageInfo.versionCode;
                }
                RudderLogger.logDebug("Current Installed Version: " + currentVersion);
                RudderLogger.logDebug("Current Installed Build: " + currentBuild);
            } catch (PackageManager.NameNotFoundException ex) {
                RudderLogger.logError(ex);
            }
        }

        /*
         * Call this method to store the Current Build and Current Version of the app.
         * In case of the LifeCycle events Application Installed or Application Updated only.
         */
        void storeCurrentBuildAndVersion() {
            preferenceManager.saveBuildNumber(currentBuild);
            preferenceManager.saveVersionName(currentVersion);
        }
    }
}
