package com.rudderstack.react.android;

import android.app.Activity;
import android.app.Application;
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
    private static Map<String, Callback> integrationCallbacks = new HashMap<>();

    static RNRudderSdkModule instance;
    static RudderClient rudderClient;
    static RNUserSessionPlugin userSessionPlugin;
    static RNParamsConfigurator configParams;
    static boolean initialized = false;
    private static RNPreferenceManager preferenceManager;

    public RNRudderSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        instance = this;
        Application application = (Application) this.reactContext.getApplicationContext();
        preferenceManager = RNPreferenceManager.getInstance(application);

        RNLifeCycleEventListener lifeCycleEventListener = new RNLifeCycleEventListener(application);
        reactContext.addLifecycleEventListener(lifeCycleEventListener);
    }

    @Override
    public String getName() {
        return "RNRudderSdkModule";
    }

    @ReactMethod
    public void setup(ReadableMap config, ReadableMap rudderOptionsMap, Promise promise) throws InterruptedException {
        if (!isRudderClientInitializedAndReady()) {
            // create the config object
            configParams = new RNParamsConfigurator(config);
            RudderConfig.Builder configBuilder = configParams.handleConfig();

            // get the instance of RudderClient
            rudderClient = RudderClient.getInstance(
                    reactContext,
                    configParams.writeKey,
                    RNRudderAnalytics.buildWithIntegrations(configBuilder),
                    Utility.convertReadableMapToOptions(rudderOptionsMap)
            );

            // Configure session tracking
            userSessionPlugin = new RNUserSessionPlugin(
                    configParams.autoSessionTracking,
                    configParams.trackLifeCycleEvents,
                    configParams.sessionTimeout
            );
            userSessionPlugin.handleSessionTracking();

            // Track automatic lifecycle and/or screen events
            LifeCycleEvents.trackAutomaticEvents();

            // RN SDK is initialised
            initialized = true;

            // process all the factories passed and stores whether they were ready or not in the integrationMap
            if (RNRudderAnalytics.integrationList != null && RNRudderAnalytics.integrationList.size() > 0) {
                for (RudderIntegration.Factory factory : RNRudderAnalytics.integrationList) {
                    String integrationName = factory.key();
                    RudderClient.Callback callback = new NativeCallBack(integrationName);
                    RNRudderSdkModule.rudderClient.onIntegrationReady(integrationName, callback);
                }
            }
        } else {
            RudderLogger.logVerbose("Rudder Client already initialized, Ignoring the new setup call");
        }
        // finally resolve the promise to mark as completed
        promise.resolve(null);
    }

    private boolean isRudderClientInitializedAndReady() {
        if (rudderClient == null || !initialized) {
            RudderLogger.logWarn("Dropping the call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return false;
        }
        return true;
    }

    @ReactMethod
    public void track(String event, ReadableMap properties, ReadableMap options) {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        rudderClient.track(new RudderMessageBuilder()
                .setEventName(event)
                .setProperty(Utility.convertReadableMapToMap(properties))
                .setRudderOption(Utility.convertReadableMapToOptions(options))
                .build());
    }

    @ReactMethod
    public void screen(String event, ReadableMap properties, ReadableMap options) {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        userSessionPlugin.saveEventTimestamp();
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
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        if (TextUtils.isEmpty(userId)) {
            rudderClient.identify(Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
            return;
        }
        rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
    }

    @ReactMethod
    public void alias(String newId, ReadableMap options) {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        if (TextUtils.isEmpty(newId)) {
            RudderLogger.logWarn("Dropping the Alias call as newId can not be empty");
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        rudderClient.alias(newId, Utility.convertReadableMapToOptions(options));
    }

    @ReactMethod
    public void group(String groupId, ReadableMap traits, ReadableMap options) {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        if (TextUtils.isEmpty(groupId)) {
            RudderLogger.logWarn("Dropping the Group call as groupId can not be empty");
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        rudderClient.group(groupId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
    }

    // Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
    @ReactMethod
    public void getRudderContext(Promise promise) throws JSONException {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        Gson gson = new Gson();
        JSONObject contextJson = new JSONObject(gson.toJson(rudderClient.getRudderContext()));
        promise.resolve(Utility.convertJSONObjectToWriteAbleMap(contextJson));
    }

    @ReactMethod
    public void reset() {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        rudderClient.reset();
    }

    @ReactMethod
    public void flush() {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        rudderClient.flush();
    }

    @ReactMethod
    public void optOut(boolean optOut) {
        if (!isRudderClientInitializedAndReady()) {
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
    public void startSession(String sessionId) {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        userSessionPlugin.enableManualSessionParams();
        if (sessionId.length() == 0) {
            userSessionPlugin.startSession();
            RudderLogger.logVerbose("RNRudderSdkModule: startSession: starting manual session");
            return;
        }
        userSessionPlugin.startSession(Long.parseLong(sessionId));
        RudderLogger.logVerbose("RNRudderSdkModule: startSession: starting manual session with id: " + sessionId);
    }

    @ReactMethod
    public void endSession() {
        if (!isRudderClientInitializedAndReady()) {
            return;
        }
        userSessionPlugin.endSession();
        RudderLogger.logVerbose("RNRudderSdkModule: endSession: ending session");
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
