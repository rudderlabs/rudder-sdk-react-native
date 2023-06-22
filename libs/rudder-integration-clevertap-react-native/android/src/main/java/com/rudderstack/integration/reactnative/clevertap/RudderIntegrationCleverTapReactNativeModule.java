package com.rudderstack.integration.reactnative.clevertap;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integrations.clevertap.CleverTapIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationCleverTapReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderIntegrationCleverTapReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderIntegrationCleverTapReactNative";
  }

  @ReactMethod
  public void setup(Promise promise) {
    RNRudderAnalytics.addIntegration(CleverTapIntegrationFactory.FACTORY);
    promise.resolve(null);
  }
}
