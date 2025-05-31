package java.com.rudderstack.react.android;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.module.annotations.ReactModule;
import com.rudderstack.react.android.NativeRudderBridgeSpec;
import com.rudderstack.react.android.RNRudderSdkModuleImpl;


@ReactModule(name = RNRudderSdkModuleImpl.NAME)
public class RNRudderSdkModule extends NativeRudderBridgeSpec {

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

    @Override
    public void setup(ReadableMap config, ReadableMap rudderOptionsMap, Promise promise) {
        moduleImpl.setup(config, rudderOptionsMap, promise);
    }

    @Override
    public void track(String event, ReadableMap properties, ReadableMap options, Promise promise) {
        moduleImpl.track(event, properties, options, promise);
    }

    @Override
    public void screen(String event, @Nullable ReadableMap properties, @Nullable ReadableMap options, Promise promise) {
        moduleImpl.screen(event, properties, options, promise);
    }

    @Override
    public void putDeviceToken(String token, Promise promise) {
        moduleImpl.putDeviceToken(token, promise);
    }

    @Override
    public void identify(String userId, @Nullable ReadableMap traits, @Nullable ReadableMap options, Promise promise) {
        moduleImpl.identify(userId, traits, options, promise);
    }

    @Override
    public void alias(String newId, @Nullable String previousId, @Nullable ReadableMap options, Promise promise) {
        moduleImpl.alias(newId, previousId, options, promise);
    }

    @Override
    public void group(String groupId, @Nullable ReadableMap traits, @Nullable ReadableMap options, Promise promise) {
        moduleImpl.group(groupId, traits, options, promise);
    }

    // Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
    @Override
    public void getRudderContext(Promise promise) {
        moduleImpl.getRudderContext(promise);
    }

    @Override
    public void reset(boolean clearAnonymousId, Promise promise) {
        moduleImpl.reset(clearAnonymousId, promise);
    }

    @Override
    public void flush(Promise promise) {
        moduleImpl.flush(promise);
    }

    @Override
    public void optOut(boolean optOut, Promise promise) {
        moduleImpl.optOut(optOut, promise);
    }

    @Override
    public void putAdvertisingId(String id, Promise promise) {
        moduleImpl.putAdvertisingId(id, promise);
    }

    @Override
    public void clearAdvertisingId(Promise promise) {
        moduleImpl.clearAdvertisingId(promise);
    }

    @Override
    public void putAnonymousId(String id, Promise promise) {
        moduleImpl.putAnonymousId(id, promise);
    }

    @Override
    public void startSession(@Nullable String sessionId, Promise promise) {
        moduleImpl.startSession(sessionId, promise);
    }

    @Override
    public void endSession(Promise promise) {
        moduleImpl.endSession(promise);
    }

    @Override
    public void getSessionId(Promise promise) {
        moduleImpl.getSessionId(promise);
    }

    @Override
    public void registerCallback(String name, Callback callback) {
        moduleImpl.registerCallback(name, callback);
    }
}
