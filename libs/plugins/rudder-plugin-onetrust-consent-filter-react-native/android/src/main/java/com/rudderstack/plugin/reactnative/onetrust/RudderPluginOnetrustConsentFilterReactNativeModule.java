package com.rudderstack.plugin.reactnative.onetrust;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.sdk.core.consent.RudderConsentFilter;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderPluginOnetrustConsentFilterReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderPluginOnetrustConsentFilterReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderPluginOnetrustConsentFilterReactNative";
  }

  private OnetrustConsentFilterPlugin onetrustConsentFilterPlugin;
  @ReactMethod
  public void startConsentFilterPlugin(Promise promise) { // TODO: Add Onetrust SDK initialization parameters
    this.onetrustConsentFilterPlugin = new OnetrustConsentFilterPlugin(this.reactContext, promise);
    this.onetrustConsentFilterPlugin.setupOneTrust();
  }

  @ReactMethod
  public void setup(Promise promise) {
    RudderConsentFilter rudderOneTrustConsentFilter = this.onetrustConsentFilterPlugin.getRudderOneTrustConsentFilter();
    if (rudderOneTrustConsentFilter != null) {
      RNRudderAnalytics.setConsentFilterPlugin(rudderOneTrustConsentFilter);
    }
    promise.resolve(null);
  }
}
