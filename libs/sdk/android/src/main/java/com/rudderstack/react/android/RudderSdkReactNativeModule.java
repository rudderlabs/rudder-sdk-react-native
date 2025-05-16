//package com.rudderstack.react.android;
//
//import androidx.annotation.Nullable;
//
//import com.facebook.react.bridge.Promise;
//import com.facebook.react.bridge.ReactApplicationContext;
//import com.facebook.react.bridge.ReadableMap;
//import com.facebook.react.module.annotations.ReactModule;
//import com.rudderstack.ruddersdkreactnative.NativeRudderSdkReactNativeSpec;
//
//@ReactModule(name = RudderSdkReactNativeModule.NAME)
//public class RudderSdkReactNativeModule extends NativeRudderSdkReactNativeSpec {
//
//  public static final String NAME = "RudderSdkReactNative";
//
//  public RudderSdkReactNativeModule(ReactApplicationContext reactContext) {
//    super(reactContext);
//  }
//
//  @Override
//  public String getName() {
//    return NAME;
//  }
//
//  // Example method
//  // See https://reactnative.dev/docs/native-modules-android
//  @Override
//  public double multiply(double a, double b) {
//    return a * b;
//  }
//
//  @Override
//  public void track(String event, ReadableMap properties, ReadableMap options, Promise promise) {
//    throw new UnsupportedOperationException("Not yet implemented");
//  }
//
//  @Override
//  public void screen(String name, @Nullable ReadableMap properties, @Nullable ReadableMap options, Promise promise) {
//
//  }
//
//  @Override
//  public void identify(String userId, @Nullable ReadableMap traits, @Nullable ReadableMap options, Promise promise) {
//
//  }
//
//  @Override
//  public void alias(String newId, @Nullable String previousId, @Nullable ReadableMap options, Promise promise) {
//
//  }
//
//  @Override
//  public void group(String groupId, @Nullable ReadableMap traits, @Nullable ReadableMap options, Promise promise) {
//
//  }
//
//  @Override
//  public void reset(boolean clearAnonymousId, Promise promise) {
//
//  }
//
//  @Override
//  public void flush(Promise promise) {
//
//  }
//
//  @Override
//  public void optOut(boolean optOut, Promise promise) {
//
//  }
//
//  @Override
//  public void putDeviceToken(String token, Promise promise) {
//
//  }
//
//  @Override
//  public void putAdvertisingId(String id, Promise promise) {
//
//  }
//
//  @Override
//  public void clearAdvertisingId(Promise promise) {
//
//  }
//
//  @Override
//  public void putAnonymousId(String id, Promise promise) {
//
//  }
//
//  @Override
//  public void startSession(@Nullable String sessionId, Promise promise) {
//
//  }
//
//  @Override
//  public void endSession(Promise promise) {
//
//  }
//
//  @Override
//  public void getSessionId(Promise promise) {
//
//  }
//}
