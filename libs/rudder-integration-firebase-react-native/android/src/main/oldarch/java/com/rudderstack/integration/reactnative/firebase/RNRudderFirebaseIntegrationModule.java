package com.rudderstack.integration.reactnative.firebase;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNRudderFirebaseIntegrationModule extends ReactContextBaseJavaModule {

    private final RNRudderFirebaseIntegrationModuleImpl moduleImpl;

    public RNRudderFirebaseIntegrationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.moduleImpl = new RNRudderFirebaseIntegrationModuleImpl(reactContext);
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
