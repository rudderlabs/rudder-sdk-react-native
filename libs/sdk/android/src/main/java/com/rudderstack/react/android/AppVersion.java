package com.rudderstack.react.android;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.rudderstack.android.sdk.core.RudderLogger;

class AppVersion {
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
