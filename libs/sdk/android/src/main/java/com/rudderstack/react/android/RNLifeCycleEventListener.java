package com.rudderstack.react.android;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.bridge.LifecycleEventListener;

import static com.rudderstack.react.android.LifeCycleEvents.executeRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationStatusRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationOpenedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationBackgroundedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ScreenViewRunnable;

public class RNLifeCycleEventListener implements LifecycleEventListener {

    private int noOfActivities;
    private boolean fromBackground = false;
    private final RNUserSessionPlugin userSessionPlugin;
    private final RNRudderSdkModule instance;
    private final boolean trackLifeCycleEvents;
    private boolean recordScreenViews;

    RNLifeCycleEventListener(Application application, RNUserSessionPlugin userSessionPlugin, RNRudderSdkModule instance, boolean trackLifeCycleEvents, boolean recordScreenViews) {
        this.userSessionPlugin = userSessionPlugin;
        this.instance = instance;
        this.trackLifeCycleEvents = trackLifeCycleEvents;
        this.recordScreenViews = recordScreenViews;
        ApplicationStatusRunnable applicationStatus = new ApplicationStatusRunnable(application, this.userSessionPlugin, this.trackLifeCycleEvents);
        executeRunnable(applicationStatus);
    }

    @Override
    public void onHostResume() {
        noOfActivities += 1;
        if (noOfActivities == 1) {
            // no previous activity present. Application Opened
            ApplicationOpenedRunnable openedRunnable = new ApplicationOpenedRunnable(fromBackground, this.userSessionPlugin, this.trackLifeCycleEvents);
            executeRunnable(openedRunnable);
        }
        Activity activity = this.instance.getCurrentActivityFromReact();
        if (activity != null && activity.getLocalClassName() != null) {
            ScreenViewRunnable screenViewRunnable = new ScreenViewRunnable(activity.getLocalClassName(), this.userSessionPlugin, this.recordScreenViews);
            executeRunnable(screenViewRunnable);
        }
    }

    @Override
    public void onHostPause() {
        fromBackground = true;
        noOfActivities -= 1;
        if (noOfActivities == 0) {
            ApplicationBackgroundedRunnable backgroundedRunnable = new ApplicationBackgroundedRunnable(this.userSessionPlugin, this.trackLifeCycleEvents);
            executeRunnable(backgroundedRunnable);
        }
    }

    @Override
    public void onHostDestroy() {
    }
}
