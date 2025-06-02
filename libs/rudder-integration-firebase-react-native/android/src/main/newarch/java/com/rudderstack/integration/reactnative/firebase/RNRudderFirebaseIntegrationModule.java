package java.com.rudderstack.integration.reactnative.firebase;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.annotations.ReactModule;
import com.rudderstack.integration.reactnative.firebase.NativeFirebaseBridgeSpec;
import com.rudderstack.integration.reactnative.firebase.RNRudderFirebaseIntegrationModuleImpl;

@ReactModule(name = RNRudderFirebaseIntegrationModuleImpl.NAME)
public class RNRudderFirebaseIntegrationModule extends NativeFirebaseBridgeSpec {

    private final RNRudderFirebaseIntegrationModuleImpl moduleImpl;

    public RNRudderFirebaseIntegrationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.moduleImpl = new RNRudderFirebaseIntegrationModuleImpl();
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
