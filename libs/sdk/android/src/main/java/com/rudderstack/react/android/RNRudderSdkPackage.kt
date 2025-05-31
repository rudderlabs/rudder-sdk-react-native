package com.rudderstack.react.android;

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import java.com.rudderstack.react.android.RNRudderSdkModule
import java.util.HashMap

class RNRudderSdkPackage : BaseReactPackage() {

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == RNRudderSdkModuleImpl.NAME) {
            RNRudderSdkModule(reactContext)
        } else {
            null
        }
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
            val isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
            moduleInfos[RNRudderSdkModuleImpl.NAME] = ReactModuleInfo(
                RNRudderSdkModuleImpl.NAME,
                RNRudderSdkModuleImpl.NAME,
                false,  // canOverrideExistingModule
                false,  // needsEagerInit
                false,  // isCxxModule
                isTurboModule    // isTurboModule
            )
            moduleInfos
        }
    }
}
