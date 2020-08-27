package com.rudderstack.integration.reactnative.appsflyer;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.rudderstack.android.integrations.appsflyer.AppsFlyerIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationAppsflyerReactNativeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RudderIntegrationAppsflyerReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RudderIntegrationAppsflyerReactNative";
    }

    @ReactMethod
    public void setup() {
          RNRudderAnalytics.addIntegration(AppsFlyerIntegrationFactory.FACTORY);
    }
}
