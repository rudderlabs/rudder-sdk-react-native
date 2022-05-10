import bridge from './bridge';
import { NativeEventEmitter, NativeModules } from 'react-native';

const { RudderIntegrationAppsflyerReactNative } = NativeModules;
const appsFlyerEventEmitter = new NativeEventEmitter(RudderIntegrationAppsflyerReactNative);

var devKey = "";
var appleAppId = "";
var isDebug = false;
var onInstallConversionDataListener = true;
var onDeepLinkListener = false;

async function setup() {
    await bridge.setup(devKey, isDebug === true, onInstallConversionDataListener === true, onDeepLinkListener === true, appleAppId);
}

function setOptions(options) {
    if (options == null) {
        throw new Error('RudderSDK: Warn: Options is Invalid, Aborting Appsflyer Initialization');
    }
    if (options.devKey) {
        if (typeof options.devKey !== 'string' || typeof options.devKey === 'undefined') {
            throw new Error('RudderSDK: Warn: devKey should be a string!');
        }
        devKey = options.devKey;
    }
    if (options.appleAppId) {
        if (typeof options.appleAppId !== 'string' || typeof options.appleAppId === 'undefined') {
            throw new Error('RudderSDK: Warn: appleAppId should be a string!');
        }
        appleAppId = options.appleAppId;
    }
    if (options.isDebug) {
        if (typeof options.isDebug !== 'boolean') {
            console.log('RudderSDK: Warn: isDebug should be a boolean!');
            return;
        }
        isDebug = options.isDebug
    }
    if (options.onInstallConversionDataListener) {
        if (typeof options.onInstallConversionDataListener !== 'boolean') {
            console.log('RudderSDK: Warn: onInstallConversionDataListener should be a boolean!');
            return;
        }
        onInstallConversionDataListener = options.onInstallConversionDataListener
    }
    if (options.onDeepLinkListener) {
        if (typeof options.onDeepLinkListener !== 'boolean') {
            console.log('RudderSDK: Warn: onDeepLinkListener should be a boolean!');
            return;
        }
        onDeepLinkListener = options.onDeepLinkListener
    }
}

function onInstallConversionData(callback) {
    const listener = appsFlyerEventEmitter.addListener('onInstallConversionDataLoaded', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback('Invalid data structure');
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
}

function onInstallConversionFailure(callback) {
    const listener = appsFlyerEventEmitter.addListener('onInstallConversionFailure', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback('Invalid data structure');
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

function onAppOpenAttribution(callback) {
    const listener = appsFlyerEventEmitter.addListener('onAppOpenAttribution', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback('Invalid data structure');
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

function onAttributionFailure(callback) {
    const listener = appsFlyerEventEmitter.addListener('onAttributionFailure', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback(new AFParseJSONException('Invalid data structure', _data));
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

function onDeepLink(callback) {
    const listener = appsFlyerEventEmitter.addListener('onDeepLinking', (_data) => {
        if (callback && typeof callback === typeof Function) {
            try {
                let data = JSON.parse(_data);
                callback(data);
            } catch (_error) {
                callback(new AFParseJSONException('Invalid data structure', _data));
            }
        }
    });

    // unregister listener (suppose should be called from componentWillUnmount() )
    return function remove() {
        listener.remove();
    };
};

/**
 * Manually record the location of the user
 *
 * @param longitude latitude as double.
 * @param latitude latitude as double.
 * @param callback success callback function
 */
 function logLocation (longitude, latitude, callback) {
	if (longitude == null || latitude == null || longitude == '' || latitude == '') {
		console.log('longitude or latitude are missing!');
		return;
	}
	if (typeof longitude != 'number' || typeof latitude != 'number') {
		longitude = parseFloat(longitude);
		latitude = parseFloat(latitude);
	}
	if (callback) {
		return bridge.logLocation(longitude, latitude, callback);
	} else {
		return bridge.logLocation(longitude, latitude, (result) => console.log(result));
	}
};

/**
 * Set the user emails and encrypt them.
 *
 * @param options latitude as double.
 * @param successC success callback function.
 * @param errorC error callback function.
 */
 function setUserEmails(options, successC, errorC) {
	return bridge.setUserEmails(options, successC, errorC);
};

/**
 * Set additional data to be sent to AppsFlyer.
 *
 * @param additionalData additional data Dictionary.
 * @param successC success callback function.
 */
function setAdditionalData(additionalData, successC) {
	if (successC) {
		return bridge.setAdditionalData(additionalData, successC);
	} else {
		return bridge.setAdditionalData(additionalData, (result) => console.log(result));
	}
};

/**
 * Get AppsFlyer's unique device ID is created for every new install of an app.
 *
 * @callback callback function that returns (error,uid)
 */
function getAppsFlyerUID (callback) {
	return bridge.getAppsFlyerUID(callback);
};

/**
 * Manually pass the Firebase / GCM Device Token for Uninstall measurement.
 *
 * @param token Firebase Device Token.
 * @param successC success callback function.
 */
function updateServerUninstallToken (token, successC) {
	if (token == null) {
		token = '';
	}
	if (typeof token != 'string') {
		token = token.toString();
	}
	if (successC) {
		return bridge.updateServerUninstallToken(token, successC);
	} else {
		return bridge.updateServerUninstallToken(token, (result) => console.log(result));
	}
};

/**
 * Setting your own customer ID enables you to cross-reference your own unique ID with AppsFlyer’s unique ID and the other devices’ IDs.
 * This ID is available in AppsFlyer CSV reports along with Postback APIs for cross-referencing with your internal IDs.
 *
 * @param {string} userId Customer ID for client.
 * @param successC callback function.
 */
function setCustomerUserId(userId, successC) {
	if (userId == null) {
		userId = '';
	}
	if (typeof userId != 'string') {
		userId = userId.toString();
	}
	if (successC) {
		return bridge.setCustomerUserId(userId, successC);
	} else {
		return bridge.setCustomerUserId(userId, (result) => console.log(result));
	}
};

/**
 * Once this API is invoked, our SDK no longer communicates with our servers and stops functioning.
 * In some extreme cases you might want to shut down all SDK activity due to legal and privacy compliance.
 * This can be achieved with the stop API.
 *
 * @param {boolean} isStopped boolean should SDK be stopped.
 * @param successC callback function.
 */
function stop(isStopped, successC) {
	if (successC) {
		return bridge.stop(isStopped, successC);
	} else {
		return bridge.stop(isStopped, (result) => console.log(result));
	}
};

/**
 * Opt-out of collection of IMEI.
 * If the app does NOT contain Google Play Services, device IMEI is collected by the SDK.
 * However, apps with Google play services should avoid IMEI collection as this is in violation of the Google Play policy.
 *
 * @param {boolean} isCollect boolean, false to opt out.
 * @param successC callback function.
 * @platform android
 */
function setCollectIMEI(isCollect, successC) {
	return bridge.setCollectIMEI(isCollect, successC);
};

/**
 * Opt-out of collection of Android ID.
 * If the app does NOT contain Google Play Services, Android ID is collected by the SDK.
 * However, apps with Google play services should avoid Android ID collection as this is in violation of the Google Play policy.
 *
 * @param {boolean} isCollect boolean, false to opt out.
 * @param successC callback function.
 * @platform android
 */
function setCollectAndroidID(isCollect, successC) {
	return bridge.setCollectAndroidID(isCollect, successC);
};

/**
 * Set the OneLink ID that should be used for User-Invite-API.
 * The link that is generated for the user invite will use this OneLink as the base link.
 *
 * @param {string} oneLinkID OneLink ID obtained from the AppsFlyer Dashboard.
 * @param successC callback function.
 */
function setAppInviteOneLinkID(oneLinkID, successC) {
	if (oneLinkID == null) {
		oneLinkID = '';
	}
	if (typeof oneLinkID != 'string') {
		oneLinkID = oneLinkID.toString();
	}
	if (successC) {
		return bridge.setAppInviteOneLinkID(oneLinkID, successC);
	} else {
		return bridge.setAppInviteOneLinkID(oneLinkID, (result) => console.log(result));
	}
};

/**
 * The LinkGenerator class builds the invite URL according to various setter methods which allow passing on additional information on the click.
 * @see https://support.appsflyer.com/hc/en-us/articles/115004480866-User-invite-attribution-
 *
 * @param parameters Dictionary.
 * @param success success callback function..
 * @param error error callback function.
 */
function generateInviteLink (parameters, success, error) {
	return bridge.generateInviteLink(parameters, success, error);
};

/**
 * To attribute an impression use the following API call.
 * Make sure to use the promoted App ID as it appears within the AppsFlyer dashboard.
 *
 * @param appId promoted App ID.
 * @param campaign cross promotion campaign.
 * @param parameters additional params to be added to the attribution link
 */
function logCrossPromotionImpression(appId, campaign, parameters)  {
	if (appId == null || appId == '') {
		console.log('appid is missing!');
		return;
	}
	if (campaign == null) {
		campaign = '';
	}
	if (typeof appId != 'string' || typeof campaign != 'string') {
		appId = appId.toString();
		campaign = campaign.toString();
	}
	return bridge.logCrossPromotionImpression(appId, campaign, parameters);
};

/**
 * Use the following API to attribute the click and launch the app store's app page.
 *
 * @param appId promoted App ID.
 * @param campaign cross promotion campaign.
 * @param params additional user params.
 */
function logCrossPromotionAndOpenStore(appId, campaign, params) {
	if (appId == null || appId == '') {
		console.log('appid is missing!');
		return;
	}
	if (campaign == null) {
		campaign = '';
	}
	if (typeof appId != 'string' || typeof campaign != 'string') {
		appId = appId.toString();
		campaign = campaign.toString();
	}
	return bridge.logCrossPromotionAndOpenStore(appId, campaign, params);
};

/**
 * Setting user local currency code for in-app purchases.
 * The currency code should be a 3 character ISO 4217 code. (default is USD).
 * You can set the currency code for all events by calling the following method.
 * @param currencyCode
 * @param successC success callback function.
 */
function setCurrencyCode(currencyCode, successC) {
	if (currencyCode == null || currencyCode == '') {
		console.log('currencyCode is missing!');
		return;
	}
	if (typeof currencyCode != 'string') {
		currencyCode = currencyCode.toString();
	}
	if (successC) {
		return bridge.setCurrencyCode(currencyCode, successC);
	} else {
		return bridge.setCurrencyCode(currencyCode, (result) => console.log(result));
	}
};

/**
 * Anonymize user Data.
 * Use this API during the SDK Initialization to explicitly anonymize a user's installs, events and sessions.
 * Default is false
 * @param shouldAnonymize boolean
 * @param successC success callback function.
 */
 function anonymizeUser(shouldAnonymize, successC) {
	if (successC) {
		return bridge.anonymizeUser(shouldAnonymize, successC);
	} else {
		return bridge.anonymizeUser(shouldAnonymize, (result) => console.log(result));
	}
};

/**
 * Set Onelink custom/branded domains
 * Use this API during the SDK Initialization to indicate branded domains.
 * For more information please refer to https://support.appsflyer.com/hc/en-us/articles/360002329137-Implementing-Branded-Links
 * @param domains array of strings
 * @param successC success callback function.
 * @param errorC error callback function.
 */
function setOneLinkCustomDomains(domains, successC, errorC) {
	return bridge.setOneLinkCustomDomains(domains, successC, errorC);
};

/**
 * Set domains used by ESP when wrapping your deeplinks.
 * Use this API during the SDK Initialization to indicate that links from certain domains should be resolved
 * in order to get original deeplink
 * For more information please refer to https://support.appsflyer.com/hc/en-us/articles/360001409618-Email-service-provider-challenges-with-iOS-Universal-links
 * @param urls array of strings
 * @param successC success callback function.
 * @param errorC error callback function.
 */
function setResolveDeepLinkURLs(urls, successC, errorC) {
	return bridge.setResolveDeepLinkURLs(urls, successC, errorC);
};

/**
 * This function allows developers to manually re-trigger onAppOpenAttribution with a specific link (URI or URL),
 * without recording a new re-engagement.
 * This method may be required if the app needs to redirect users based on the given link,
 * or resolve the AppsFlyer short URL while staying in the foreground/opened. This might be needed because
 * regular onAppOpenAttribution callback is only called if the app was opened with the deep link.
 * @param urlString String representing the URL that needs to be resolved/returned in the onAppOpenAttribution callback
 * @param callback Result callback
 */
function performOnAppAttribution(urlString, successC, errorC)  {
	if (typeof urlString != 'string') {
		urlString = urlString.toString();
	}
	return bridge.performOnAppAttribution(urlString, successC, errorC);
};

/**
 * Disables IDFA collection in iOS and Advertising ID in Android
 * @param shouldDisable Flag to disable/enable IDFA collection
 */
function disableAdvertisingIdentifier(isDisable) {
	return bridge.disableAdvertisingIdentifier(isDisable);
};

/**
 * Disables Apple Search Ads collecting
 * @param shouldDisable Flag to disable/enable Apple Search Ads data collection
 * @platform iOS only
 */
function disableCollectASA(shouldDisable) {
	return bridge.disableCollectASA(shouldDisable);
};

/**
 * Receipt validation is a secure mechanism whereby the payment platform (e.g. Apple or Google) validates that an in-app purchase indeed occurred as reported.
 * Learn more - https://support.appsflyer.com/hc/en-us/articles/207032106-Receipt-validation-for-in-app-purchases
 * @param purchaseInfo ReadableMap includes: String publicKey, String signature, String purchaseData, String price, String currency, JSONObject additionalParameters.
 * @param successC Success callback
 * @param errorC Error callback
 */
function validateAndLogInAppPurchase(purchaseInfo, successC, errorC) {
	return bridge.validateAndLogInAppPurchase(purchaseInfo, successC, errorC);
};

function setUseReceiptValidationSandbox(isSandbox) {
	return bridge.setUseReceiptValidationSandbox(isSandbox);
};

/**
 *
 *Push-notification campaigns are used to create fast re-engagements with existing users.
 *AppsFlyer supplies an open-for-all solution, that enables measuring the success of push-notification campaigns, for both iOS and Android platforms.
 * Learn more - https://support.appsflyer.com/hc/en-us/articles/207364076-Measuring-Push-Notification-Re-Engagement-Campaigns
 * @param pushPayload
 */
function sendPushNotificationData(pushPayload) {
	return bridge.sendPushNotificationData(pushPayload);
};

/**
 * Set a custom host
 * @param hostPrefix
 * @param hostName
 * @param successC: success callback
 */
function setHost(hostPrefix, hostName, successC) {
	bridge.setHost(hostPrefix, hostName, successC);
};

/**
 * The addPushNotificationDeepLinkPath method provides app owners with a flexible interface for configuring how deep links are extracted from push notification payloads.
 * for more information: https://support.appsflyer.com/hc/en-us/articles/207032126-Android-SDK-integration-for-developers#core-apis-65-configure-push-notification-deep-link-resolution
 * @param path: an array of string that represents the path
 * @param successC: success callback
 * @param errorC: error callback
 */
function addPushNotificationDeepLinkPath(path, successC, errorC) {
	bridge.addPushNotificationDeepLinkPath(path, successC, errorC);
};

/**
 * enable or disable SKAD support. set True if you want to disable it!
 * @param isDisabled
 */
function disableSKAD(disableSkad) {
	return bridge.disableSKAD(disableSkad);
};

/**
 * Set the language of the device. The data will be displayed in Raw Data Reports
 * @param language
 */
function setCurrentDeviceLanguage(language) {
	if (typeof language === 'string') {
		return bridge.setCurrentDeviceLanguage(language);
	}
};

/**
 *  Used by advertisers to exclude specified networks/integrated partners from getting data.
 */
function setSharingFilterForPartners(partners)  {
	return bridge.setSharingFilterForPartners(partners);
};
/**
 * Allows sending custom data for partner integration purposes.
 * @param partnerId: ID of the partner (usually suffixed with "_int").
 * @param partnerData: Customer data, depends on the integration configuration with the specific partner.
 */
function setPartnerData(partnerId, partnerData) {
	if (typeof partnerId === 'string' && typeof partnerData === 'object') {
		return bridge.setPartnerData(partnerId, partnerData);
	}
};


export { onDeepLink, onInstallConversionData, onInstallConversionFailure, onAppOpenAttribution, onAttributionFailure, setOptions };
export default setup;