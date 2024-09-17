package com.rudderstack.plugin.reactnative.ketch

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import java.lang.Thread.sleep

class KetchConsentFilterPlugin(
    reactContext: ReactApplicationContext,
    private val promise: Promise,
) {

    fun setupKetch() {
        // This is a dummy thread to simulate the delay in the Ketch SDK initialization and will not be present in the actual implementation
        // TODO: Remove this
        Thread {
            sleep(2000)
//            promise.resolve(false)
            promise.resolve(true)
        }.start()
    }

    private fun initKetchConsentFilterPlugin() {
        promise.resolve(true)
    }

    private fun handleConsentDenied() {
        promise.resolve(false)
    }
}
