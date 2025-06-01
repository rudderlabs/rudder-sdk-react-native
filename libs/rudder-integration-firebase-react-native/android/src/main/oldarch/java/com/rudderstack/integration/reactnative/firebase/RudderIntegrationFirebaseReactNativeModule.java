package com.rudderstack.integration.reactnative.firebase;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RudderIntegrationFirebaseReactNativeModule extends ReactContextBaseJavaModule {

    private final RudderIntegrationFirebaseReactNativeModuleImpl moduleImpl;

    public RudderIntegrationFirebaseReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.moduleImpl = new RudderIntegrationFirebaseReactNativeModuleImpl(reactContext);
    }

    @Override
    public String getName() {
        return moduleImpl.getName();
    }

    @ReactMethod
    public void setup(Promise promise) {
        moduleImpl.setup(promise);
    }
}
