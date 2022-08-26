package com.rudderstack.react.android;

import android.app.Activity;

import com.facebook.react.bridge.LifecycleEventListener;
import com.rudderstack.android.sdk.core.RudderProperty;
import com.rudderstack.android.sdk.core.ScreenPropertyBuilder;

import java.util.ArrayList;
import java.util.List;

public class RNLifeCycleEventListener implements LifecycleEventListener {

    private static int noOfActivities;
    private static boolean fromBackground = false;
    static List<Runnable> runnableTasks = new ArrayList<>();

    @Override
    public void onHostResume() {
        Runnable runnableTask = new Runnable() {
            @Override
            public void run() {
                if (RNRudderSdkModule.trackLifeCycleEvents) {
                    noOfActivities += 1;
                    if (noOfActivities == 1) {
                        // no previous activity present. Application Opened
                        RudderProperty property = new RudderProperty();
                        property.put("from_background", fromBackground);
                        RNRudderSdkModule.rudderClient.track("Application Opened", property);
                    }
                }
                if (RNRudderSdkModule.recordScreenViews) {
                    Activity activity = RNRudderSdkModule.instance.getCurrentActivityFromReact();
                    RudderProperty property = new RudderProperty();
                    property.put("name", activity.getLocalClassName());
                    property.put("automatic", true);
                    RNRudderSdkModule.rudderClient.screen(activity.getLocalClassName(), property);
                }
            }
        };
        if (RNRudderSdkModule.rudderClient == null && !RNRudderSdkModule.initialized) {
            runnableTasks.add(runnableTask);
            return;
        }
        runnableTask.run();
    }

    @Override
    public void onHostPause() {
        Runnable runnableTask = new Runnable() {
            @Override
            public void run() {
                fromBackground = true;
                if (RNRudderSdkModule.trackLifeCycleEvents) {
                    noOfActivities -= 1;
                    if (noOfActivities == 0) {
                        RNRudderSdkModule.rudderClient.track("Application Backgrounded");
                    }
                }
            }
        };
        if (RNRudderSdkModule.rudderClient == null && !RNRudderSdkModule.initialized) {
            runnableTasks.add(runnableTask);
            return;
        }
        runnableTask.run();
    }

    @Override
    public void onHostDestroy() {
    }
}
