package com.rudderstack.integration.reactnative.firebase;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integration.firebase.FirebaseIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationFirebaseReactNativeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RudderIntegrationFirebaseReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RudderIntegrationFirebaseReactNative";
    }

    @ReactMethod
    public void setup(Promise promise) {
        RNRudderAnalytics.addIntegration(FirebaseIntegrationFactory.FACTORY);
        promise.resolve(null);
    }
}
