package java.com.rudderstack.react.android;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.rudderstack.react.android.RNRudderSdkModuleImpl;

public class RNRudderSdkModule extends ReactContextBaseJavaModule {
    private final RNRudderSdkModuleImpl moduleImpl;

    public RNRudderSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        Application application = (Application) reactContext.getApplicationContext();
        this.moduleImpl = new RNRudderSdkModuleImpl(reactContext, application);
    }

    @NonNull
    @Override
    public String getName() {
        return moduleImpl.getName();
    }

    @ReactMethod
    public void setup(ReadableMap config, ReadableMap rudderOptionsMap, Promise promise) {
        moduleImpl.setup(config, rudderOptionsMap, promise);
    }

    @ReactMethod
    public void track(String event, ReadableMap properties, ReadableMap options, Promise promise) {
        moduleImpl.track(event, properties, options, promise);
    }

    @ReactMethod
    public void screen(String event, ReadableMap properties, ReadableMap options, Promise promise) {
        moduleImpl.screen(event, properties, options, promise);
    }

    @ReactMethod
    public void putDeviceToken(String token, Promise promise) {
        moduleImpl.putDeviceToken(token, promise);
    }

    @ReactMethod
    public void identify(String userId, ReadableMap traits, ReadableMap options, Promise promise) {
        moduleImpl.identify(userId, traits, options, promise);
    }

    @ReactMethod
    public void alias(String newId, String previousId, ReadableMap options, Promise promise) {
        moduleImpl.alias(newId, previousId, options, promise);
    }

    @ReactMethod
    public void group(String groupId, ReadableMap traits, ReadableMap options, Promise promise) {
        moduleImpl.group(groupId, traits, options, promise);
    }

    // Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
    @ReactMethod
    public void getRudderContext(Promise promise) {
        moduleImpl.getRudderContext(promise);
    }

    @ReactMethod
    public void reset(boolean clearAnonymousId, Promise promise) {
        moduleImpl.reset(clearAnonymousId, promise);
    }

    @ReactMethod
    public void flush(Promise promise) {
        moduleImpl.flush(promise);
    }

    @ReactMethod
    public void optOut(boolean optOut, Promise promise) {
        moduleImpl.optOut(optOut, promise);
    }

    @ReactMethod
    public void putAdvertisingId(String id, Promise promise) {
        moduleImpl.putAdvertisingId(id, promise);
    }

    @ReactMethod
    public void clearAdvertisingId(Promise promise) {
        moduleImpl.clearAdvertisingId(promise);
    }

    @ReactMethod
    public void putAnonymousId(String id, Promise promise) {
        moduleImpl.putAnonymousId(id, promise);
    }

    @ReactMethod
    public void startSession(String sessionId, Promise promise) {
        moduleImpl.startSession(sessionId, promise);
    }

    @ReactMethod
    public void endSession(Promise promise) {
        moduleImpl.endSession(promise);
    }

    @ReactMethod
    public void getSessionId(Promise promise) {
        moduleImpl.getSessionId(promise);
    }

    @ReactMethod
    public void registerCallback(String name, Callback callback) {
        moduleImpl.registerCallback(name, callback);
    }
}
