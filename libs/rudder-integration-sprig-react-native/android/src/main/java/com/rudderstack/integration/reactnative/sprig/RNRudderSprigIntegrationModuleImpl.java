package com.rudderstack.integration.reactnative.sprig;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.rudderstack.android.integration.sprig.SprigIntegrationFactory;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RNRudderSprigIntegrationModuleImpl {

    public static final String NAME = "RNRudderSprigIntegrationModule";

    private final ReactApplicationContext reactContext;
    private boolean callbacksRegistered = false;

    public RNRudderSprigIntegrationModuleImpl(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public String getName() {
        return NAME;
    }

    public void setup(Promise promise) {
        RNRudderAnalytics.addIntegration(SprigIntegrationFactory.FACTORY);
        registerActivityCallbacks();
        promise.resolve(null);
    }

    private void registerActivityCallbacks() {
        if (callbacksRegistered) {
            return;
        }
        Application application = RudderClient.getApplication();
        if (application == null) {
            return;
        }

        // Seed factory with the currently visible activity so surveys can present immediately
        // after setup, instead of waiting for the next resume.
        Activity current = reactContext.getCurrentActivity();
        if (current instanceof FragmentActivity) {
            SprigIntegrationFactory.FACTORY.setFragmentActivity((FragmentActivity) current);
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                // NO-OP
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                // NO-OP
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                if (activity instanceof FragmentActivity) {
                    SprigIntegrationFactory.FACTORY.setFragmentActivity((FragmentActivity) activity);
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                if (activity instanceof FragmentActivity) {
                    SprigIntegrationFactory.FACTORY.setFragmentActivity(null);
                }
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                // NO-OP
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
                // NO-OP
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                // NO-OP
            }
        });
        callbacksRegistered = true;
    }
}
