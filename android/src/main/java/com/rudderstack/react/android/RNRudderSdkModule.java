package com.rudderstack.react.android;

import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderContext;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderProperty;
import com.rudderstack.android.sdk.core.RudderMessageBuilder;
import com.rudderstack.android.sdk.core.RudderTraits;
import com.rudderstack.android.sdk.core.util.Utils;

import java.util.HashMap;
import java.util.Map;

import com.facebook.react.bridge.Callback;

import java.lang.InterruptedException;

public class RNRudderSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  public static Boolean integrationReady = null;
  private static int deviceModeCallBackThreshold;
  private static Map<String, Callback> integrationCallbacks = new HashMap<>();

  private RudderClient rudderClient;

  public RNRudderSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNRudderSdkModule";
  }

  @ReactMethod
  public void setup(ReadableMap options, ReadableMap rudderOptionsMap, Promise promise) throws InterruptedException {
    if (rudderClient == null) {

      String writeKey = options.getString("writeKey");

      // build RudderConfig to get RudderClient instance
      RudderConfig.Builder configBuilder = new RudderConfig.Builder();
      if (options.hasKey("dataPlaneUrl")) {
        configBuilder.withDataPlaneUrl(options.getString("dataPlaneUrl"));
      }
      if (options.hasKey("controlPlaneUrl")) {
        configBuilder.withControlPlaneUrl(options.getString("controlPlaneUrl"));
      }
      if (options.hasKey("flushQueueSize")) {
        configBuilder.withFlushQueueSize(options.getInt("flushQueueSize"));
      }
      if (options.hasKey("dbCountThreshold")) {
        configBuilder.withDbThresholdCount(options.getInt("dbCountThreshold"));
      }
      if (options.hasKey("sleepTimeOut")) {
        configBuilder.withSleepCount(options.getInt("sleepTimeOut"));
      }
      if (options.hasKey("configRefreshInterval")) {
        configBuilder.withConfigRefreshInterval(options.getInt("configRefreshInterval"));
      }
      if (options.hasKey("deviceModeCallBackThreshold")) {
        deviceModeCallBackThreshold = options.getInt("deviceModeCallBackThreshold");
      }
      if (options.hasKey("trackAppLifecycleEvents")) {
        configBuilder.withTrackLifecycleEvents(options.getBoolean("trackAppLifecycleEvents"));
      }
      if (options.hasKey("recordScreenViews")) {
        configBuilder.withRecordScreenViews(options.getBoolean("recordScreenViews"));
      }
      if (options.hasKey("logLevel")) {
        configBuilder.withLogLevel(options.getInt("logLevel"));
      }

      // get the instance of RudderClient
      rudderClient = RudderClient.getInstance(
          reactContext,
          writeKey,
          RNRudderAnalytics.buildWithIntegrations(configBuilder),
          Utility.convertReadableMapToOptions(rudderOptionsMap)
          );
      rudderClient.track("Application Opened");
      // process all the factories passed and stores whether they were ready or not in the integrationMap
      if (RNRudderAnalytics.integrationList != null && RNRudderAnalytics.integrationList.size() > 0) {
        for (RudderIntegration.Factory factory : RNRudderAnalytics.integrationList) {
          String integrationName = factory.key();
          RudderClient.Callback callback = new NativeCallBack(integrationName);
          rudderClient.onIntegrationReady(integrationName, callback);
        }
      }
    } else {
      RudderLogger.logVerbose("Rudder Client already initialized, Ignoring the new setup call");
    }
    // finally resolve the promise to mark as completed
    promise.resolve(null);
  }

  @ReactMethod
  public void track(String event, ReadableMap properties, ReadableMap options) {
    if (rudderClient == null) {
      return;
    }
    rudderClient.track(new RudderMessageBuilder()
        .setEventName(event)
        .setProperty(Utility.convertReadableMapToMap(properties))
        .setRudderOption(Utility.convertReadableMapToOptions(options))
        .build());
  }

  @ReactMethod
  public void screen(String event, ReadableMap properties, ReadableMap options) {
    if (rudderClient == null) {
      return;
    }
    RudderProperty property = new RudderProperty();
    property.putValue(Utility.convertReadableMapToMap(properties));
    rudderClient.screen(event,property, Utility.convertReadableMapToOptions(options));
  }

  @ReactMethod
  public void putDeviceToken(String token) {
    if (rudderClient == null) {
      return;
    }
    if (!TextUtils.isEmpty(token)) {
      rudderClient.putDeviceToken(token);
    }
  }

  @ReactMethod
  public void identify(String userId, ReadableMap traits, ReadableMap options) {
    if (rudderClient == null) {
      return;
    }
    rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
  }

  @ReactMethod
  public void reset() {
    if (rudderClient == null) {
      return;
    }
    rudderClient.reset();
  }

  @ReactMethod
  public void setAdvertisingId(String id) {
    RudderClient.updateWithAdvertisingId(id);
  }

  @ReactMethod
  public void setAnonymousId(String id) {
    RudderClient.setAnonymousId(id);
  }

  // will check if the passed Integration exists or not and respond accordingly
  @ReactMethod
  public void checkIntegrationReady(String integrationName, Promise promise) {
    if (rudderClient == null) {
      promise.resolve(false);
    }
    if (RNRudderSdkModule.integrationMap.containsKey(integrationName)) {
      if (RNRudderSdkModule.integrationMap.get(integrationName) != null) {
        promise.resolve(true);
      }
    }
    promise.resolve(false);
  }

  @ReactMethod
  public void registerCallback(String name, Callback callBack, Promise promise) {
    if (rudderClient == null) {
      promise.resolve(false);
    }
    integrationCallbacks.put(name, callBack);
    promise.resolve(true);
  }

  class NativeCallBack implements RudderClient.Callback {
    private String integrationName;
    NativeCallBack(String integrationName) {
      this.integrationName = integrationName;
    }

    @Override
    public void onReady(Object instance) {
      if (integrationCallbacks.containsKey(integrationName)){
        integrationCallbacks.get(integrationName).invoke();
      }
    }
  }
}
