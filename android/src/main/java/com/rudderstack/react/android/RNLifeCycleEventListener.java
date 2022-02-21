package com.rudderstack.react.android;

import com.facebook.react.bridge.LifecycleEventListener;

import java.util.ArrayList;
import java.util.List;

public class RNLifeCycleEventListener implements LifecycleEventListener {

    private static int noOfActivities;
    static List<Runnable> runnableTasks = new ArrayList<>();

    @Override
    public void onHostResume() {
        System.out.println("onHostResume");
        Runnable runnableTask = new Runnable() {
            @Override
            public void run() {
                if (RNRudderSdkModule.trackLifeCycleEvents) {
                    noOfActivities += 1;
                    if (noOfActivities == 1) {
                        // no previous activity present. Application Opened
                        RNRudderSdkModule.rudderClient.track("Application Opened");
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
    public void onHostPause() {
        System.out.println("onHostPause");
        Runnable runnableTask = new Runnable() {
            @Override
            public void run() {
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
        System.out.println("onHostDestroy");
    }
}
