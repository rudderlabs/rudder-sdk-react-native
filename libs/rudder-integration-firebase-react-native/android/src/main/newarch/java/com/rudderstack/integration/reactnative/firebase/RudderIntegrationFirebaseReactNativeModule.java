package com.rudderstack.integration.reactnative.firebase;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.annotations.ReactModule;
import com.rudderstack.integration.reactnative.firebase.NativeFirebaseBridgeSpec;

@ReactModule(name = RudderIntegrationFirebaseReactNativeModuleImpl.NAME)
public class RudderIntegrationFirebaseReactNativeModule extends NativeFirebaseBridgeSpec {

    private final RudderIntegrationFirebaseReactNativeModuleImpl moduleImpl;

    public RudderIntegrationFirebaseReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.moduleImpl = new RudderIntegrationFirebaseReactNativeModuleImpl(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return moduleImpl.getName();
    }

    @Override
    public void setup(Promise promise) {
        moduleImpl.setup(promise);
    }
}
