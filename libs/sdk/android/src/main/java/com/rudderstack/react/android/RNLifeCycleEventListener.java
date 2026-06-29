package com.rudderstack.react.android;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.facebook.react.bridge.LifecycleEventListener;

import static com.rudderstack.react.android.LifeCycleEvents.executeRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationStatusRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationOpenedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ApplicationBackgroundedRunnable;
import static com.rudderstack.react.android.LifeCycleEvents.ScreenViewRunnable;

/**
 * Tracks application lifecycle (Application Opened / Application Backgrounded) at the PROCESS
 * level via androidx ProcessLifecycleOwner, mirroring the native rudder-sdk-android
 * implementation. ProcessLifecycleOwner only transitions on genuine app foreground/background
 * (it is debounced across activity transitions), so in-app activity changes - e.g. a
 * third-party SDK popping a consent dialog / paywall / ad Activity over the React host - no
 * longer produce spurious Application Opened/Backgrounded loops.
 *
 * Automatic screen views stay on React Native's host lifecycle (onHostResume), as they need the
 * current Activity.
 */
public class RNLifeCycleEventListener implements LifecycleEventListener, DefaultLifecycleObserver {

    private boolean fromBackground = false;
    private final RNUserSessionPlugin userSessionPlugin;
    private final RNRudderSdkModuleImpl instance;
    private final boolean trackLifeCycleEvents;
    private final boolean recordScreenViews;

    RNLifeCycleEventListener(Application application, RNUserSessionPlugin userSessionPlugin, RNRudderSdkModuleImpl instance, boolean trackLifeCycleEvents, boolean recordScreenViews) {
        this.userSessionPlugin = userSessionPlugin;
        this.instance = instance;
        this.trackLifeCycleEvents = trackLifeCycleEvents;
        this.recordScreenViews = recordScreenViews;
        ApplicationStatusRunnable applicationStatus = new ApplicationStatusRunnable(application, this.userSessionPlugin, this.trackLifeCycleEvents);
        executeRunnable(applicationStatus);
    }

    // ProcessLifecycleOwner: genuine app foreground. Fires once on cold start (adding the
    // observer replays the already-STARTED state) and again on every real return from background.
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        ApplicationOpenedRunnable openedRunnable = new ApplicationOpenedRunnable(fromBackground, this.userSessionPlugin, this.trackLifeCycleEvents);
        executeRunnable(openedRunnable);
    }

    // ProcessLifecycleOwner: genuine app background.
    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        fromBackground = true;
        ApplicationBackgroundedRunnable backgroundedRunnable = new ApplicationBackgroundedRunnable(this.userSessionPlugin, this.trackLifeCycleEvents);
        executeRunnable(backgroundedRunnable);
    }

    // React Native host lifecycle: used only for automatic screen view tracking (needs the
    // current Activity).
    @Override
    public void onHostResume() {
        Activity activity = this.instance.getCurrentActivityFromReact();
        if (activity != null && activity.getLocalClassName() != null) {
            ScreenViewRunnable screenViewRunnable = new ScreenViewRunnable(activity.getLocalClassName(), this.userSessionPlugin, this.recordScreenViews);
            executeRunnable(screenViewRunnable);
        }
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }
}
