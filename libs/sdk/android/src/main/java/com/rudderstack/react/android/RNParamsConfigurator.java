package com.rudderstack.react.android;

import com.facebook.react.bridge.ReadableMap;
import com.rudderstack.android.sdk.core.RudderConfig;

class RNParamsConfigurator {
    private final ReadableMap rudderOptionsMap;
    boolean trackLifeCycleEvents = true;
    boolean recordScreenViews = false;
    long sessionTimeout = 300000L;
    boolean autoSessionTracking = true;
    String writeKey;

    RNParamsConfigurator(ReadableMap rudderOptionsMap) {
        this.rudderOptionsMap = rudderOptionsMap;
    }

    RudderConfig.Builder handleConfig() {
        saveConfigValues();
        saveWriteKey();
        RudderConfig.Builder configBuilder = buildConfig();
        disableAutoConfigFlagsForNativeSDK(configBuilder);
        return configBuilder;
    }

    private void saveWriteKey() {
        if (this.rudderOptionsMap.hasKey("writeKey")) {
            writeKey = rudderOptionsMap.getString("writeKey");
        } else {
            throw new IllegalArgumentException("writeKey is required");
        }
    }

    private void saveConfigValues() {
        if (this.rudderOptionsMap.hasKey("trackAppLifecycleEvents")) {
            trackLifeCycleEvents = rudderOptionsMap.getBoolean("trackAppLifecycleEvents");
        }
        if (this.rudderOptionsMap.hasKey("recordScreenViews")) {
            recordScreenViews = rudderOptionsMap.getBoolean("recordScreenViews");
        }
        if (this.rudderOptionsMap.hasKey("sessionTimeout")) {
            sessionTimeout = (long) rudderOptionsMap.getDouble("sessionTimeout");
        }
        if (this.rudderOptionsMap.hasKey("autoSessionTracking")) {
            autoSessionTracking = rudderOptionsMap.getBoolean("autoSessionTracking");
        }
    }

    private RudderConfig.Builder buildConfig() {
        RudderConfig.Builder configBuilder = new RudderConfig.Builder();
        if (rudderOptionsMap.hasKey("dataPlaneUrl")) {
            configBuilder.withDataPlaneUrl(rudderOptionsMap.getString("dataPlaneUrl"));
        }
        if (rudderOptionsMap.hasKey("controlPlaneUrl")) {
            configBuilder.withControlPlaneUrl(rudderOptionsMap.getString("controlPlaneUrl"));
        }
        if (rudderOptionsMap.hasKey("flushQueueSize")) {
            configBuilder.withFlushQueueSize(rudderOptionsMap.getInt("flushQueueSize"));
        }
        if (rudderOptionsMap.hasKey("dbCountThreshold")) {
            configBuilder.withDbThresholdCount(rudderOptionsMap.getInt("dbCountThreshold"));
        }
        if (rudderOptionsMap.hasKey("sleepTimeOut")) {
            configBuilder.withSleepCount(rudderOptionsMap.getInt("sleepTimeOut"));
        }
        if (rudderOptionsMap.hasKey("configRefreshInterval")) {
            configBuilder.withConfigRefreshInterval(rudderOptionsMap.getInt("configRefreshInterval"));
        }
        if (rudderOptionsMap.hasKey("autoCollectAdvertId")) {
            configBuilder.withAutoCollectAdvertId(rudderOptionsMap.getBoolean("autoCollectAdvertId"));
        }
        if (rudderOptionsMap.hasKey("logLevel")) {
            configBuilder.withLogLevel(rudderOptionsMap.getInt("logLevel"));
        }
        return configBuilder;
    }

    private void disableAutoConfigFlagsForNativeSDK(RudderConfig.Builder configBuilder) {
        // We are relying on the RN implementation in RNLifeCycleEventListener and RNUserSessionPlugin hence we are explicitly setting all flags to false in Native Android SDK
        configBuilder.withRecordScreenViews(false);
        configBuilder.withTrackLifecycleEvents(false);
        configBuilder.withAutoSessionTracking(false);
    }
}