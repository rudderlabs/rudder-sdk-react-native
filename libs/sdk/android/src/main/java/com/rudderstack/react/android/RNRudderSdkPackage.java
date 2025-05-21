package com.rudderstack.react.android;

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
        if (name.equals(RNRudderSdkModule.NAME)) {
            return new RNRudderSdkModule(reactContext);
        } else {
            return null;
        }
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return new ReactModuleInfoProvider() {
            @Override
            public Map<String, ReactModuleInfo> getReactModuleInfos() {
                Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
                moduleInfos.put(RNRudderSdkModule.NAME, new ReactModuleInfo(
                        RNRudderSdkModule.NAME,
                        RNRudderSdkModule.NAME,
                        false,  // canOverrideExistingModule
                        false,  // needsEagerInit
                        false,  // isCxxModule
                        true    // isTurboModule
                ));
                return moduleInfos;
            }
        };
    }
}
