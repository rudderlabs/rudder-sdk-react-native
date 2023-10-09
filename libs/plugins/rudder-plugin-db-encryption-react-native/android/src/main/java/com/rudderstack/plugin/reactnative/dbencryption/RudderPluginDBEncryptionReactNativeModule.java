package com.rudderstack.plugin.reactnative.dbencryption;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderPluginDBEncryptionReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderPluginDBEncryptionReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderPluginDBEncryptionReactNative";
  }

  @ReactMethod
  public void setup(String key, Boolean enable, Promise promise) {
    RudderConfig.DBEncryption dbEncryption = new RudderConfig.DBEncryption(enable, key);
    RNRudderAnalytics.setDBEncryption(dbEncryption);
    promise.resolve(null);
  }
}
