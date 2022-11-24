package com.rudderstack.react.android;

import com.rudderstack.android.sdk.core.RudderProperty;

import java.util.ArrayList;
import java.util.List;

public class LifeCycleEvents {

    interface LifeCycleEventsInterface {
        public void run();
    }

    static List<LifeCycleEventsInterface> lifeCycleEvents = new ArrayList<>();

    static class ApplicationOpenedRunnable implements LifeCycleEventsInterface {
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

    static class ApplicationBackgroundedRunnable implements LifeCycleEventsInterface {
        @Override
        public void run() {
            if (RNRudderSdkModule.trackLifeCycleEvents) {
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
            if (RNRudderSdkModule.recordScreenViews) {
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