package com.rudderstack.integration.reactnative.firebase;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.rudderstack.android.integration.firebase.FirebaseIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RNRudderFirebaseIntegrationModuleImpl {

    public static final String NAME = "RNRudderFirebaseIntegrationModule";

    private final ReactApplicationContext reactContext;

    public RNRudderFirebaseIntegrationModuleImpl(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public String getName() {
        return NAME;
    }

    public void setup(Promise promise) {
        try {
            RNRudderAnalytics.addIntegration(FirebaseIntegrationFactory.FACTORY);
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject("ERROR", e.getMessage());
        }
    }
}
