package com.rudderstack.integration.reactnative.amplitude;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integrations.amplitude.AmplitudeIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationAmplitudeReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderIntegrationAmplitudeReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderIntegrationAmplitudeReactNative";
  }

  @ReactMethod
  public void setup() {
    RNRudderAnalytics.addIntegration(AmplitudeIntegrationFactory.FACTORY);
  }
}
