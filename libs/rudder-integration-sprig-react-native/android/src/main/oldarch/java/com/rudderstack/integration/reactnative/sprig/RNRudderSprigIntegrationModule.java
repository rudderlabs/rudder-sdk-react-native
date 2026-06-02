package java.com.rudderstack.integration.reactnative.sprig;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.integration.reactnative.sprig.RNRudderSprigIntegrationModuleImpl;

public class RNRudderSprigIntegrationModule extends ReactContextBaseJavaModule {

    private final RNRudderSprigIntegrationModuleImpl moduleImpl;

    public RNRudderSprigIntegrationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.moduleImpl = new RNRudderSprigIntegrationModuleImpl(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return moduleImpl.getName();
    }

    @ReactMethod
    public void setup(Promise promise) {
        moduleImpl.setup(promise);
    }
}
