
package com.reactlibrary;

import android.app.Application;
import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

public class RNRudderSdkPackage implements ReactPackage {
    private static Application applicationContext;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        applicationContext = (Application) reactContext.getApplicationContext();
        return Arrays.<NativeModule>asList(new RNRudderSdkModule(reactContext));
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @ReactMethod
    public static void _initiateInstance(
            String _writeKey,
            String _endPointUrl,
            int _flushQueueSize,
            int _dbCountThreshold,
            int _sleepTimeout
    ) {
        RudderClient._initiateInstance(
                applicationContext,
                _writeKey,
                _endPointUrl,
                _flushQueueSize,
                _dbCountThreshold,
                _sleepTimeout
        );
    }

    @ReactMethod
    public static void _logEvent(
            String _eventType,
            String _eventName,
            String _userId,
            String _eventPropsJson,
            String _userPropsJson,
            String _integrationsJson
    ) {
        RudderClient._logEvent(
                _eventType,
                _eventName,
                _userId,
                _eventPropsJson,
                _userPropsJson,
                _integrationsJson
        );
    }
}