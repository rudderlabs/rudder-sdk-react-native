package com.rudderstack.integration.reactnative.firebase

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import java.util.HashMap

class RNRudderFirebaseIntegrationPackage : BaseReactPackage() {

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == RNRudderFirebaseIntegrationModuleImpl.NAME) {
            RNRudderFirebaseIntegrationModule(reactContext)
        } else {
            null
        }
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
            val isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
            moduleInfos[RNRudderFirebaseIntegrationModuleImpl.NAME] = ReactModuleInfo(
                RNRudderFirebaseIntegrationModuleImpl.NAME,
                RNRudderFirebaseIntegrationModuleImpl.NAME,
                false,  // canOverrideExistingModule
                false,  // needsEagerInit
                false,  // isCxxModule
                isTurboModule    // isTurboModule
            )
            moduleInfos
        }
    }
}
