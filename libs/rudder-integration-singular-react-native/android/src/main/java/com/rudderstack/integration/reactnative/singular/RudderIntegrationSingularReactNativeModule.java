package com.rudderstack.integration.reactnative.singular;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integration.singular.SingularIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderIntegrationSingularReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderIntegrationSingularReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderIntegrationSingularReactNative";
  }

  @ReactMethod
  public void setup(Promise promise) {
    RNRudderAnalytics.addIntegration(SingularIntegrationFactory.FACTORY);
    promise.resolve(null);
  }
}
