package com.rudderstack.plugin.reactnative.ketch

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.onetrust.otpublishers.headless.Public.DataModel.OTSdkParams
import com.onetrust.otpublishers.headless.Public.OTCallback
import com.onetrust.otpublishers.headless.Public.OTEventListener
import com.onetrust.otpublishers.headless.Public.OTPublishersHeadlessSDK
import com.onetrust.otpublishers.headless.Public.OTUIDisplayReason.OTUIDisplayReason
import com.onetrust.otpublishers.headless.Public.Response.OTResponse
import com.rudderstack.android.consentfilter.onetrustconsentfilter.RudderOneTrustConsentFilter
import com.rudderstack.android.sdk.core.consent.RudderConsentFilter
import java.lang.Thread.sleep

class KetchConsentFilterPlugin(
    reactContext: ReactApplicationContext,
    private val promise: Promise,
) {
    private val otPublishersHeadlessSDK by lazy { OTPublishersHeadlessSDK(reactContext) }

    var rudderOneTrustConsentFilter: RudderConsentFilter? = null
        private set

    fun setupOneTrust() {
        otPublishersHeadlessSDK.startSDK(
            "<STORAGE_LOCATION>", //cdn
            "<DOMAIN_IDENTIFIER>", // domainIdentifier
            "<LANGUAGE>", // language code
            OTSdkParams.SdkParamsBuilder.newInstance().build(),
            object : OTCallback {
                override fun onSuccess(otSuccessResponse: OTResponse) {}
                override fun onFailure(otErrorResponse: OTResponse) {}
            })
        otPublishersHeadlessSDK.addEventListener(oneTrustEventListener)

        // This is a dummy thread to simulate the delay in the OneTrust SDK initialization and will not be present in the actual implementation
        // TODO: Remove this
        Thread {
            sleep(2000)
//            promise.resolve(false)
            promise.resolve(true)
        }.start()
    }

    private val oneTrustEventListener = object : OTEventListener() {
        override fun onHideBanner() {
            initKetchConsentFilterPlugin()
        }

        override fun onBannerClickedAcceptAll() {
            initKetchConsentFilterPlugin()
        }

        override fun onBannerClickedRejectAll() {
            initKetchConsentFilterPlugin()
        }

        override fun onPreferenceCenterAcceptAll() {
            initKetchConsentFilterPlugin()
        }

        override fun onPreferenceCenterRejectAll() {
            initKetchConsentFilterPlugin()
        }

        override fun onPreferenceCenterConfirmChoices() {
            initKetchConsentFilterPlugin()
        }

        override fun onShowPreferenceCenter(p0: OTUIDisplayReason?) {
            handleConsentDenied()
        }
        override fun onHidePreferenceCenter() {
            handleConsentDenied()
        }
        override fun onShowVendorList() {
            handleConsentDenied()
        }
        override fun onHideVendorList() {
            handleConsentDenied()
        }
        override fun onShowBanner(p0: OTUIDisplayReason?) {
            handleConsentDenied()
        }
        override fun onVendorConfirmChoices() {}
        override fun allSDKViewsDismissed(p0: String?) {
            handleConsentDenied()
        }
        override fun onVendorListVendorConsentChanged(p0: String?, p1: Int) {
            handleConsentDenied()
        }
        override fun onVendorListVendorLegitimateInterestChanged(p0: String?, p1: Int) {
            handleConsentDenied()
        }
        override fun onPreferenceCenterPurposeConsentChanged(p0: String?, p1: Int) {
            handleConsentDenied()
        }
        override fun onPreferenceCenterPurposeLegitimateInterestChanged(p0: String?, p1: Int) {
            handleConsentDenied()
        }
    }

    private fun initKetchConsentFilterPlugin() {
        this.rudderOneTrustConsentFilter = RudderOneTrustConsentFilter(otPublishersHeadlessSDK)
        promise.resolve(true)
    }

    private fun handleConsentDenied() {
        promise.resolve(false)
    }


}
