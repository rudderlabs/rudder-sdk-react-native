package com.rudderstack.react.android;

import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableNativeMap;
import com.google.gson.Gson;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderProperty;
import com.rudderstack.android.sdk.core.RudderMessageBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.lang.InterruptedException;

public class RNRudderSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    public static Boolean integrationReady = null;
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
        rudderClient.screen(event, property, Utility.convertReadableMapToOptions(options));
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
        if(TextUtils.isEmpty(userId))
        {
            rudderClient.identify(Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
            return;
        }
        rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
    }

    // Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
    @ReactMethod
    public void getRudderContext(Promise promise) throws JSONException {
        if (rudderClient == null) {
            promise.resolve(null);
            return;
        }
        Gson gson = new Gson();
        JSONObject contextJson = new JSONObject(gson.toJson(rudderClient.getRudderContext()));
        promise.resolve(Utility.convertJSONObjectToWriteAbleMap(contextJson));
    }

    @ReactMethod
    public void reset() {
        if (rudderClient == null) {
            return;
        }
        rudderClient.reset();
    }

    @ReactMethod
    public void optOut(boolean optOut) {
        if (rudderClient == null) {
            RudderLogger.logWarn("Dropping the optOut call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return;
        }
        rudderClient.optOut(optOut);
    }

    @ReactMethod
    public void setAdvertisingId(String id) {
        RudderClient.updateWithAdvertisingId(id);
    }

    @ReactMethod
    public void setAnonymousId(String id) {
        RudderClient.setAnonymousId(id);
    }

    @ReactMethod
    public void registerCallback(String name, Callback callback) {
        integrationCallbacks.put(name, callback);
    }

    class NativeCallBack implements RudderClient.Callback {
        private String integrationName;

        NativeCallBack(String integrationName) {
            this.integrationName = integrationName;
        }

        @Override
        public void onReady(Object instance) {
            if (integrationCallbacks.containsKey(this.integrationName)) {
                Callback callback = integrationCallbacks.get(this.integrationName);
                if (callback != null) {
                    callback.invoke();
                }
            }
        }
    }
}
