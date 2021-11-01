package com.rudderstack.integration.reactnative.braze;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integrations.braze.BrazeIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationBrazeReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderIntegrationBrazeReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderIntegrationBrazeReactNative";
  }

  @ReactMethod
  public void setup() {
    RNRudderAnalytics.addIntegration(BrazeIntegrationFactory.FACTORY);
  }
}
