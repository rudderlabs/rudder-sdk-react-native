package com.rudderstack.react.android;

import android.app.Activity;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
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

    static RNRudderSdkModule instance;
    static RudderClient rudderClient;
    static boolean trackLifeCycleEvents = true;
    static boolean recordScreenViews = false;
    static boolean initialized = false;

    public RNRudderSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.instance = this;
        reactContext.addLifecycleEventListener(new RNLifeCycleEventListener());
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
                trackLifeCycleEvents = options.getBoolean("trackAppLifecycleEvents");
                configBuilder.withTrackLifecycleEvents(options.getBoolean("trackAppLifecycleEvents"));
            }
            if (options.hasKey("recordScreenViews")) {
                recordScreenViews = options.getBoolean("recordScreenViews");
            }
            
            // we are relying on Screen View Recording implementation in RNLifeCycleEventListener.java hence we are explicitly setting it to false in Native Android SDK
            configBuilder.withRecordScreenViews(false);

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
            for (Runnable runnableTask : RNLifeCycleEventListener.runnableTasks) {
                runnableTask.run();
            }
            initialized = true;

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
            RudderLogger.logWarn("Dropping the Track call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
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
            RudderLogger.logWarn("Dropping the Screen call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return;
        }
        RudderProperty property = new RudderProperty();
        property.putValue(Utility.convertReadableMapToMap(properties));
        rudderClient.screen(event, property, Utility.convertReadableMapToOptions(options));
    }

    @ReactMethod
    public void putDeviceToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            RudderClient.putDeviceToken(token);
        }
    }

    @ReactMethod
    public void identify(String userId, ReadableMap traits, ReadableMap options) {
        if (rudderClient == null) {
            RudderLogger.logWarn("Dropping the Identify call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            rudderClient.identify(Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
            return;
        }
        rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
    }

    @ReactMethod
    public void alias(String newId, ReadableMap options) {
        if (rudderClient == null) {
            RudderLogger.logWarn("Dropping the Alias call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return;
        }
        if (TextUtils.isEmpty(newId)) {
            RudderLogger.logWarn("Dropping the Alias call as newId can not be empty");
            return;
        }
        rudderClient.alias(newId, Utility.convertReadableMapToOptions(options));
    }

    @ReactMethod
    public void group(String groupId, ReadableMap traits, ReadableMap options) {
        if (rudderClient == null) {
            RudderLogger.logWarn("Dropping the Group call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return;
        }
        if (TextUtils.isEmpty(groupId)) {
            RudderLogger.logWarn("Dropping the Group call as groupId can not be empty");
            return;
        }
        rudderClient.group(groupId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
    }

    // Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
    @ReactMethod
    public void getRudderContext(Promise promise) throws JSONException {
        if (rudderClient == null) {
            RudderLogger.logWarn("Dropping the getRudderContext call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
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
            RudderLogger.logWarn("Dropping the Reset call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
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
    public void putAdvertisingId(String id) {
        if (!TextUtils.isEmpty(id)) {
            RudderClient.putAdvertisingId(id);
        }
    }

    @ReactMethod
    public void putAnonymousId(String id) {
        if (!TextUtils.isEmpty(id)) {
            RudderClient.putAnonymousId(id);
        }
    }

    @ReactMethod
    public void registerCallback(String name, Callback callback) {
        integrationCallbacks.put(name, callback);
    }

    Activity getCurrentActivityFromReact() {
        return getCurrentActivity();
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
