
package com.reactlibrary;

import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessageBuilder;
import com.rudderstack.android.sdk.core.RudderTraits;

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
        // check for the writeKey
        if (TextUtils.isEmpty(writeKey)) {
            promise.reject("E_RUDDER_SDK", "WriteKey can't be null");
        }

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
        if (options.hasKey("logLevel")) {
            configBuilder.withLogLevel(options.getInt("logLevel"));
        }

        // get the instance of RudderClient
        rudderClient = RudderClient.getInstance(reactContext, writeKey, configBuilder.build());

        // finally resolve the promise to mark as completed
        promise.resolve(null);
    }

    @ReactMethod
    public void track(String event, ReadableMap properties, ReadableMap userProperties, ReadableMap options) {
        if (rudderClient == null) return;
        if (event == null) {
            RudderLogger.logError("Track: Mandatory field missing: Event name");
            return;
        }
        rudderClient.track(new RudderMessageBuilder()
                .setEventName(event)
                .setProperty(Utility.convertReadableMapToMap(properties))
                .setUserProperty(Utility.convertReadableMapToMap(userProperties))
                .build());
    }

    @ReactMethod
    public void screen(String event, ReadableMap properties, ReadableMap userProperties, ReadableMap options) {
        if (rudderClient == null) return;
        if (event == null) {
            RudderLogger.logError("Screen: Mandatory field missing: Event name");
            return;
        }
        rudderClient.screen(new RudderMessageBuilder()
                .setEventName(event)
                .setProperty(Utility.convertReadableMapToMap(properties))
                .setUserProperty(Utility.convertReadableMapToMap(userProperties))
                .build());
    }

    @ReactMethod
    public void identify(String userId, ReadableMap traits, ReadableMap options) {
        if (rudderClient == null) return;
        if(userId.length() > 0) {
            rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), null);
        } else {
            RudderTraits _traits = Utility.convertReadableMapToTraits(traits);
            if (_traits == null) {
                RudderLogger.logError("Identify: Mandatory field missing: Atleast one of userId or Traits is mandatory");
                return;
            }
            rudderClient.identify(_traits, null);
        }
    }

    @ReactMethod
    public void reset() {
        if (rudderClient == null) return;
        rudderClient.reset();
    }

    @ReactMethod
    public String getAnonymousId () {
        if (rudderClient == null) return null;
        return "0000";
    }
}