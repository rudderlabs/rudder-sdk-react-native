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
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessageBuilder;
import com.rudderstack.android.sdk.core.RudderTraits;
import com.rudderstack.android.sdk.core.util.Utils;

public class RNRudderSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

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
    public void setup(ReadableMap options, Promise promise) {
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
                RNRudderAnalytics.buildWithIntegrations(configBuilder)
        );
        rudderClient.track("Application Opened");

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
                .build());
    }

    @ReactMethod
    public void screen(String event, ReadableMap properties, ReadableMap options) {
        if (rudderClient == null) {
          return;
        }
        rudderClient.screen(new RudderMessageBuilder()
                .setEventName(event)
                .setProperty(Utility.convertReadableMapToMap(properties))
                .build());
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
        rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), null);
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
        if (rudderClient == null) {
          return;
        }
        rudderClient.updateWithAdvertisingId(id);
    }

    @ReactMethod
    public void setAnonymousId(String id) {
        if (rudderClient == null) {
          return;
        }
        rudderClient.setAnonymousId(id);
    }
}
