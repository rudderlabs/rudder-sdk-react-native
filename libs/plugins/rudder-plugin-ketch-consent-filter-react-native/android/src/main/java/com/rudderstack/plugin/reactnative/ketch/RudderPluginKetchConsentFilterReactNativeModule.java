package com.rudderstack.plugin.reactnative.ketch;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

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
    this.ketchConsentFilterPlugin.setupKetch();
  }

  @ReactMethod
  public void setup(Promise promise) {
    // Set the consent filter plugin
    promise.resolve(null);
  }
}
