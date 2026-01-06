package com.example

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeApplicationEntryPoint.loadReactNative
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.LogConfig

class MainApplication : Application(), ReactApplication {

  override val reactHost: ReactHost by lazy {
    getDefaultReactHost(
      context = applicationContext,
      packageList =
        PackageList(this).packages.apply {
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // add(MyReactNativePackage())
        },
    )
  }

  override fun onCreate() {
    super.onCreate()
    loadReactNative(this)
    // Needed to initialise the MoEngage SDK:
    if (!BuildConfig.MOENGAGE_ANDROID_APP_ID.isEmpty()) {
      val moEngage = MoEngage.Builder(this, BuildConfig.MOENGAGE_ANDROID_APP_ID)
        .configureLogs(LogConfig(LogLevel.VERBOSE, false))
        .build()
      MoEngage.initialiseDefaultInstance(moEngage)
    }
  }
}
