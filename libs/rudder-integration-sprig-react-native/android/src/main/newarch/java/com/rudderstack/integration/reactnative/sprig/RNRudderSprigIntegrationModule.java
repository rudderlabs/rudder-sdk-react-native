package java.com.rudderstack.integration.reactnative.sprig;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.annotations.ReactModule;
import com.rudderstack.integration.reactnative.sprig.NativeSprigBridgeSpec;
import com.rudderstack.integration.reactnative.sprig.RNRudderSprigIntegrationModuleImpl;

@ReactModule(name = RNRudderSprigIntegrationModuleImpl.NAME)
public class RNRudderSprigIntegrationModule extends NativeSprigBridgeSpec {

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

    @Override
    public void setup(Promise promise) {
        moduleImpl.setup(promise);
    }
}
