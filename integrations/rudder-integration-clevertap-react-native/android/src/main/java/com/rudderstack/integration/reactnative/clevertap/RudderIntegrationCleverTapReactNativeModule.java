package com.rudderstack.integration.reactnative.clevertap;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integration.clevertap.CleverTapIntegrationFactory;
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
  public void setup() {
    RNRudderAnalytics.addIntegration(CleverTapIntegrationFactory.FACTORY);
  }
}
