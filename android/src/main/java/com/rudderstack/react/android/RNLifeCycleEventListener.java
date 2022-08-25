package com.rudderstack.react.android;

import android.app.Activity;

import com.facebook.react.bridge.LifecycleEventListener;

import static com.rudderstack.react.android.LifeCycleRunnables.executeRunnable;
import static com.rudderstack.react.android.LifeCycleRunnables.ApplicationOpenedRunnable;
import static com.rudderstack.react.android.LifeCycleRunnables.ApplicationBackgroundedRunnable;
import static com.rudderstack.react.android.LifeCycleRunnables.ScreenViewRunnable;

public class RNLifeCycleEventListener implements LifecycleEventListener {

    private static int noOfActivities;
    private static boolean fromBackground = false;

    @Override
    public void onHostResume() {
        noOfActivities += 1;
        if (noOfActivities == 1) {
            // no previous activity present. Application Opened
            ApplicationOpenedRunnable openedRunnable = new ApplicationOpenedRunnable(fromBackground);
            executeRunnable(openedRunnable);
        }
        Activity activity = RNRudderSdkModule.instance.getCurrentActivityFromReact();
        if (activity != null && activity.getLocalClassName() != null) {
            ScreenViewRunnable screenViewRunnable = new ScreenViewRunnable(activity.getLocalClassName());
            executeRunnable(screenViewRunnable);
        }
    }

    @Override
    public void onHostPause() {
        fromBackground = true;
        noOfActivities -= 1;
        if (noOfActivities == 0) {
            ApplicationBackgroundedRunnable backgroundedRunnable = new ApplicationBackgroundedRunnable();
            executeRunnable(backgroundedRunnable);
        }
    }

    @Override
    public void onHostDestroy() {
    }
}
