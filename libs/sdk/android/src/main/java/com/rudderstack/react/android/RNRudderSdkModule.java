package com.rudderstack.react.android;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.module.annotations.ReactModule;
import com.google.gson.Gson;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderContext;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderProperty;
import com.rudderstack.android.sdk.core.RudderMessageBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = RNRudderSdkModule.NAME)
public class RNRudderSdkModule extends NativeRudderBridgeSpec {

    public static final String NAME = "RNRudderSdkModule";
    private final ReactApplicationContext reactContext;
    private static Map<String, Callback> integrationCallbacks = new HashMap<>();

    private RudderClient rudderClient;
    private RNUserSessionPlugin userSessionPlugin;
    private boolean initialized = false;
    private final Application application;

    public RNRudderSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.application = (Application) this.reactContext.getApplicationContext();
        RNPreferenceManager preferenceManager = RNPreferenceManager.getInstance(this.application);
        preferenceManager.migrateAppInfoPreferencesWhenRNPrefDoesNotExist();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setup(ReadableMap config, ReadableMap rudderOptionsMap, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            // create the config object
            RNParamsConfigurator configParams = new RNParamsConfigurator(config);
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
            RNLifeCycleEventListener lifeCycleEventListener = new RNLifeCycleEventListener(this.application, userSessionPlugin, this, configParams.trackLifeCycleEvents, configParams.recordScreenViews);
            reactContext.addLifecycleEventListener(lifeCycleEventListener);

            // RN SDK is initialised
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

    private boolean isRudderClientInitializedAndReady() {
        if (rudderClient == null || !initialized) {
            RudderLogger.logWarn("Dropping the call as RudderClient is not initialized yet, Please use `await` keyword with the setup call");
            return false;
        }
        return true;
    }

    @Override
    public void track(String event, ReadableMap properties, ReadableMap options, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        rudderClient.track(new RudderMessageBuilder()
                .setEventName(event)
                .setProperty(Utility.convertReadableMapToMap(properties))
                .setRudderOption(Utility.convertReadableMapToOptions(options))
                .build());
        promise.resolve(null);
    }

    @Override
    public void screen(String event, @Nullable ReadableMap properties, @Nullable ReadableMap options, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        RudderProperty property = new RudderProperty();
        property.putValue(Utility.convertReadableMapToMap(properties));
        rudderClient.screen(event, property, Utility.convertReadableMapToOptions(options));
        promise.resolve(null);
    }

    @Override
    public void putDeviceToken(String token, Promise promise) {
        if (!TextUtils.isEmpty(token)) {
            RudderClient.putDeviceToken(token);
        }
        promise.resolve(null);
    }

    @Override
    public void identify(String userId, @Nullable ReadableMap traits, @Nullable ReadableMap options, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        if (TextUtils.isEmpty(userId)) {
            rudderClient.identify(Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
            promise.resolve(null);
            return;
        }
        rudderClient.identify(userId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
        promise.resolve(null);
    }

    @Override
    public void alias(String newId, @Nullable String previousId, @Nullable ReadableMap options, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        if (TextUtils.isEmpty(newId)) {
            RudderLogger.logWarn("Dropping the Alias call as newId can not be empty");
            promise.resolve(null);
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        rudderClient.alias(newId, previousId, Utility.convertReadableMapToOptions(options));
        promise.resolve(null);
    }

    @Override
    public void group(String groupId, @Nullable ReadableMap traits, @Nullable ReadableMap options, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        if (TextUtils.isEmpty(groupId)) {
            RudderLogger.logWarn("Dropping the Group call as groupId can not be empty");
            promise.resolve(null);
            return;
        }
        userSessionPlugin.saveEventTimestamp();
        rudderClient.group(groupId, Utility.convertReadableMapToTraits(traits), Utility.convertReadableMapToOptions(options));
        promise.resolve(null);
    }

    // Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
    @Override
    public void getRudderContext(Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }

        RudderContext rudderContext = rudderClient.getRudderContext();
        if (rudderContext == null) {
            promise.resolve(null);
            return;
        }
        Gson gson = new Gson();
        JSONObject contextJson = null;
        try {
            contextJson = new JSONObject(gson.toJson(rudderContext));
            promise.resolve(Utility.convertJSONObjectToWriteAbleMap(contextJson));
        } catch (JSONException e) {
            promise.reject(e);
        }
    }

    @Override
    public void reset(boolean clearAnonymousId, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        rudderClient.reset(clearAnonymousId);
        promise.resolve(null);
    }

    @Override
    public void flush(Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        rudderClient.flush();
        promise.resolve(null);
    }

    @Override
    public void optOut(boolean optOut, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        rudderClient.optOut(optOut);
        promise.resolve(null);
    }

    @Override
    public void putAdvertisingId(String id, Promise promise) {
        if (!TextUtils.isEmpty(id)) {
            RudderClient.putAdvertisingId(id);
        }
        promise.resolve(null);
    }

    @Override
    public void clearAdvertisingId(Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        rudderClient.clearAdvertisingId();
        promise.resolve(null);
    }

    @Override
    public void putAnonymousId(String id, Promise promise) {
        if (!TextUtils.isEmpty(id)) {
            RudderClient.putAnonymousId(id);
        }
        promise.resolve(null);
    }

    @Override
    public void startSession(@Nullable String sessionId, Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        userSessionPlugin.enableManualSessionParams();
        if (sessionId.length() == 0) {
            userSessionPlugin.startSession();
            RudderLogger.logVerbose("RNRudderSdkModule: startSession: starting manual session");
            promise.resolve(null);
            return;
        }
        userSessionPlugin.startSession(Long.parseLong(sessionId));
        RudderLogger.logVerbose("RNRudderSdkModule: startSession: starting manual session with id: " + sessionId);
        promise.resolve(null);
    }

    @Override
    public void endSession(Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        userSessionPlugin.endSession();
        RudderLogger.logVerbose("RNRudderSdkModule: endSession: ending session");
        promise.resolve(null);
    }

    @Override
    public void getSessionId(Promise promise) {
        if (!isRudderClientInitializedAndReady()) {
            promise.resolve(null);
            return;
        }
        try {
            String sessionId = (userSessionPlugin.getSessionId() != null) ?
                    String.valueOf(userSessionPlugin.getSessionId()) : null;
            promise.resolve(sessionId);
        } catch(Exception e) {
            promise.reject(e);
        }
    }

    @Override
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
