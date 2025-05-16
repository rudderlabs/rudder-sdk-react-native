//package com.rudderstack.react.android;
//
//import com.facebook.react.BaseReactPackage;
//import com.facebook.react.bridge.NativeModule;
//import com.facebook.react.bridge.ReactApplicationContext;
//import com.facebook.react.module.model.ReactModuleInfo;
//import com.facebook.react.module.model.ReactModuleInfoProvider;
//import java.util.HashMap;
//import java.util.Map;
//
//public class RudderSdkReactNativePackage extends BaseReactPackage {
//  @Override
//  public NativeModule getModule(String name, ReactApplicationContext reactContext) {
//    System.out.println("Abhishek: Java");
//    if (name.equals(RudderSdkReactNativeModule.NAME)) {
//      return new RudderSdkReactNativeModule(reactContext);
//    } else {
//      return null;
//    }
//  }
//
//  @Override
//  public ReactModuleInfoProvider getReactModuleInfoProvider() {
//    return new ReactModuleInfoProvider() {
//      @Override
//      public Map<String, ReactModuleInfo> getReactModuleInfos() {
//        Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
//        moduleInfos.put(RudderSdkReactNativeModule.NAME, new ReactModuleInfo(
//                RudderSdkReactNativeModule.NAME,
//                RudderSdkReactNativeModule.NAME,
//                false,  // canOverrideExistingModule
//                false,  // needsEagerInit
//                false,  // isCxxModule
//                true    // isTurboModule
//        ));
//        return moduleInfos;
//      }
//    };
//  }
//}
