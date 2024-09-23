package com.rudderstack.integration.reactnative.facebook;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderlabs.android.integration.facebook.FacebookIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationFacebookReactNativeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RudderIntegrationFacebookReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RudderIntegrationFacebookReactNative";
    }

    @ReactMethod
    public void setup(Promise promise) {
        RNRudderAnalytics.addIntegration(FacebookIntegrationFactory.FACTORY);
        promise.resolve(null);
    }
}
