package com.rudderstack.plugin.reactnative.ketch;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.sdk.core.consent.RudderConsentFilter;
import com.rudderstack.react.android.RNRudderAnalytics;

public class RudderPluginKetchConsentFilterReactNativeModule
  extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RudderPluginKetchConsentFilterReactNativeModule(
    ReactApplicationContext reactContext
  ) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RudderPluginKetchConsentFilterReactNative";
  }

  private KetchConsentFilterPlugin ketchConsentFilterPlugin;
  @ReactMethod
  public void startConsentFilterPlugin(Promise promise) { // TODO: Add Ketch SDK initialization parameters
    this.ketchConsentFilterPlugin = new KetchConsentFilterPlugin(this.reactContext, promise);
    this.ketchConsentFilterPlugin.setupOneTrust();
  }

  @ReactMethod
  public void setup(Promise promise) {
    RudderConsentFilter rudderOneTrustConsentFilter = this.ketchConsentFilterPlugin.getRudderOneTrustConsentFilter();
    if (rudderOneTrustConsentFilter != null) {
      RNRudderAnalytics.setConsentFilterPlugin(rudderOneTrustConsentFilter);
    }
    promise.resolve(null);
  }
}
