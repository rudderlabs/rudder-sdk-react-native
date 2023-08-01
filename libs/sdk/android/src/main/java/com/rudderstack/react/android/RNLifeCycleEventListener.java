package com.rudderstack.react.android;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.bridge.LifecycleEventListener;

import static com.rudderstack.react.android.LifeCycleEvents.executeRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationInstalledOrUpdatedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationOpenedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationBackgroundedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ScreenViewRunnable;

public class RNLifeCycleEventListener implements LifecycleEventListener {

    private static int noOfActivities;
    private static boolean fromBackground = false;

    RNLifeCycleEventListener(Application application) {
      ApplicationInstalledOrUpdatedRunnable applicationInstalled = new ApplicationInstalledOrUpdatedRunnable(application);
      executeRunnable(applicationInstalled);
    }

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
