package com.rudderstack.integration.reactnative.sprig

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import java.com.rudderstack.integration.reactnative.sprig.RNRudderSprigIntegrationModule
import java.util.HashMap

class RNRudderSprigIntegrationPackage : BaseReactPackage() {

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == RNRudderSprigIntegrationModuleImpl.NAME) {
            RNRudderSprigIntegrationModule(reactContext)
        } else {
            null
        }
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
            val isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
            moduleInfos[RNRudderSprigIntegrationModuleImpl.NAME] = ReactModuleInfo(
                RNRudderSprigIntegrationModuleImpl.NAME,
                RNRudderSprigIntegrationModuleImpl.NAME,
                false,  // canOverrideExistingModule
                false,  // needsEagerInit
                false,  // isCxxModule
                isTurboModule    // isTurboModule
            )
            moduleInfos
        }
    }
}
