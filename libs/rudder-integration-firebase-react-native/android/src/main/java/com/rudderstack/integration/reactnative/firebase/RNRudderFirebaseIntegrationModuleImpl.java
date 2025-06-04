package com.rudderstack.integration.reactnative.firebase;

import com.facebook.react.bridge.Promise;
import com.rudderstack.android.integration.firebase.FirebaseIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RNRudderFirebaseIntegrationModuleImpl {

    public static final String NAME = "RNRudderFirebaseIntegrationModule";

    public RNRudderFirebaseIntegrationModuleImpl() {
    }

    public String getName() {
        return NAME;
    }

    public void setup(Promise promise) {
        RNRudderAnalytics.addIntegration(FirebaseIntegrationFactory.FACTORY);
        promise.resolve(null);
    }
}
