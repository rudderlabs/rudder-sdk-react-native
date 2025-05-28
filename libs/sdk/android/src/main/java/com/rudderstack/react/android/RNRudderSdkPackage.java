package com.rudderstack.react.android;

import java.com.rudderstack.react.android.RNRudderSdkModule;
import java.util.HashMap;
import java.util.Map;

import com.facebook.react.BaseReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;

public class RNRudderSdkPackage extends BaseReactPackage {

    @Override
    public NativeModule getModule(String name, ReactApplicationContext reactContext) {
        if (RNRudderSdkModuleImpl.NAME.equals(name)) {
            return new RNRudderSdkModule(reactContext);
        }
        return null;
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return new ReactModuleInfoProvider() {
            @Override
            public Map<String, ReactModuleInfo> getReactModuleInfos() {
                boolean isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
                Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
                moduleInfos.put(RNRudderSdkModuleImpl.NAME, new ReactModuleInfo(
                        RNRudderSdkModuleImpl.NAME,
                        RNRudderSdkModuleImpl.NAME,
                        false,  // canOverrideExistingModule
                        false,  // needsEagerInit
                        false,  // isCxxModule
                        isTurboModule    // isTurboModule
                ));
                return moduleInfos;
            }
        };
    }
}
