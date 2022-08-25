package com.rudderstack.react.android;

import com.rudderstack.android.sdk.core.RudderProperty;

import java.util.ArrayList;
import java.util.List;

public class LifeCycleRunnables {

    static List<Runnable> runnableTasks = new ArrayList<>();

    static class ApplicationOpenedRunnable implements Runnable {
        boolean fromBackground;

        ApplicationOpenedRunnable(boolean fromBackground) {
            this.fromBackground = fromBackground;
        }

        @Override
        public void run() {
            if (RNRudderSdkModule.trackLifeCycleEvents) {
                RudderProperty property = new RudderProperty();
                property.put("from_background", this.fromBackground);
                RNRudderSdkModule.rudderClient.track("Application Opened", property);
            }
        }
    }

    static class ApplicationBackgroundedRunnable implements Runnable {
        @Override
        public void run() {
            if (RNRudderSdkModule.trackLifeCycleEvents) {
                RNRudderSdkModule.rudderClient.track("Application Backgrounded");
            }
        }
    }

    static class ScreenViewRunnable implements Runnable {
        String activityName;

        ScreenViewRunnable(String activityName) {
            this.activityName = activityName;
        }

        @Override
        public void run() {
            if (RNRudderSdkModule.recordScreenViews) {
                RudderProperty property = new RudderProperty();
                property.put("name", activityName);
                property.put("automatic", true);
                RNRudderSdkModule.rudderClient.screen(activityName, property);
            }
        }
    }

    static void executeRunnable(Runnable runnable) {
        if (RNRudderSdkModule.rudderClient == null && !RNRudderSdkModule.initialized) {
            runnableTasks.add(runnable);
            return;
        }
        runnable.run();
    }
}