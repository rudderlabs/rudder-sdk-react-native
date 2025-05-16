package com.rudderstack.react.android;

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.rudderstack.ruddersdkreactnative.NativeRudderSdkReactNativeSpec

@ReactModule(name = RudderSdkReactNativeModule.NAME)
class RudderSdkReactNativeModule(reactContext: ReactApplicationContext) :
  NativeRudderSdkReactNativeSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }

  companion object {
    const val NAME = "RudderSdkReactNative"
  }
}
