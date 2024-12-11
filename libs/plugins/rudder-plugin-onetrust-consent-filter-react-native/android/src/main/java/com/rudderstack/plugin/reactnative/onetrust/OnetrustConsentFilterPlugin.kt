package com.rudderstack.plugin.reactnative.onetrust

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

class OnetrustConsentFilterPlugin(
    reactContext: ReactApplicationContext,
    private val promise: Promise,
) {

    private val otPublishersHeadlessSDK by lazy { OTPublishersHeadlessSDK(reactContext) }

    var rudderOneTrustConsentFilter: RudderConsentFilter? = null
        private set

    fun setupOneTrust(cdn: String, domainIdentifier: String, languageCode: String) {
        otPublishersHeadlessSDK.startSDK(
            cdn,
            domainIdentifier,
            languageCode,
            OTSdkParams.SdkParamsBuilder.newInstance().build(),
            object : OTCallback {
                override fun onSuccess(otSuccessResponse: OTResponse) {}
                override fun onFailure(otErrorResponse: OTResponse) {}
            })
        otPublishersHeadlessSDK.addEventListener(oneTrustEventListener)
    }

    private val oneTrustEventListener = object : OTEventListener() {
        override fun onHideBanner() {
            initOnetrustConsentFilterPlugin()
        }

        override fun onBannerClickedAcceptAll() {
            initOnetrustConsentFilterPlugin()
        }

        override fun onBannerClickedRejectAll() {
            initOnetrustConsentFilterPlugin()
        }

        override fun onPreferenceCenterAcceptAll() {
            initOnetrustConsentFilterPlugin()
        }

        override fun onPreferenceCenterRejectAll() {
            initOnetrustConsentFilterPlugin()
        }

        override fun onPreferenceCenterConfirmChoices() {
            initOnetrustConsentFilterPlugin()
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

    private fun initOnetrustConsentFilterPlugin() {
        this.rudderOneTrustConsentFilter = RudderOneTrustConsentFilter(otPublishersHeadlessSDK)
        promise.resolve(true)
    }

    private fun handleConsentDenied() {
        promise.resolve(false)
    }

}
