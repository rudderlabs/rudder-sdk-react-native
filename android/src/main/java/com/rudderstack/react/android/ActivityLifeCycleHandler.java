package com.rudderstack.react.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ActivityLifeCycleHandler implements Application.ActivityLifecycleCallbacks {

    private static int noOfActivities;
    static List<Runnable> runnableTasks = new ArrayList<>();

    ActivityLifeCycleHandler(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
    }

    static void registerActivityLifeCycleCallBacks(Context context) {
        new ActivityLifeCycleHandler(context);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        System.out.println("onActivityCreated");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        System.out.println("onActivityStarted");
        //        Runnable runnableTask = new Runnable() {
//            @Override
//            public void run() {
//                if (RNRudderSdkModule.trackLifeCycleEvents) {
//                    noOfActivities += 1;
//                    if (noOfActivities == 1) {
//                        // no previous activity present. Application Opened
//                        RNRudderSdkModule.rudderClient.track("Application Opened");
//                    }
//                }
//            }
//        };
//        if (RNRudderSdkModule.rudderClient == null && !RNRudderSdkModule.initialized) {
//            runnableTasks.add(runnableTask);
//            return;
//        }
//        runnableTask.run();
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        System.out.println("onActivityResumed");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        System.out.println("onActivityPaused");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
//        Runnable runnableTask = new Runnable() {
//            @Override
//            public void run() {
//                if (RNRudderSdkModule.trackLifeCycleEvents) {
//                    noOfActivities -= 1;
//                    if (noOfActivities == 0) {
//                        RNRudderSdkModule.rudderClient.track("Application Backgrounded");
//                    }
//                }
//            }
//        };
//        if (RNRudderSdkModule.rudderClient == null && !RNRudderSdkModule.initialized) {
//            runnableTasks.add(runnableTask);
//            return;
//        }
//        runnableTask.run();
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        System.out.println("onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        System.out.println("onActivityDestroyed");
    }
}