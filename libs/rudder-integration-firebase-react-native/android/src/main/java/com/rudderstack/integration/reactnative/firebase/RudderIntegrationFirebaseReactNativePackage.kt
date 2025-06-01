package com.rudderstack.integration.reactnative.firebase

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import java.util.HashMap

class RudderIntegrationFirebaseReactNativePackage : BaseReactPackage() {

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == RudderIntegrationFirebaseReactNativeModuleImpl.NAME) {
            RudderIntegrationFirebaseReactNativeModule(reactContext)
        } else {
            null
        }
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
            val isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
            moduleInfos[RudderIntegrationFirebaseReactNativeModuleImpl.NAME] = ReactModuleInfo(
                RudderIntegrationFirebaseReactNativeModuleImpl.NAME,
                RudderIntegrationFirebaseReactNativeModuleImpl.NAME,
                false,  // canOverrideExistingModule
                false,  // needsEagerInit
                false,  // isCxxModule
                isTurboModule    // isTurboModule
            )
            moduleInfos
        }
    }
}
