package com.rudderstack.integration.reactnative.appsflyer;

import android.app.Activity;
import android.app.Application;

import com.appsflyer.AppsFlyerLib;
import com.facebook.react.bridge.Promise;

import androidx.annotation.NonNull;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.rudderstack.android.integrations.appsflyer.AppsFlyerIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

import com.appsflyer.share.LinkGenerator;
import com.appsflyer.share.ShareInviteHelper;
import com.appsflyer.deeplink.DeepLinkResult;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.share.CrossPromotionHelper;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerProperties.EmailsCryptType;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static com.appsflyer.reactnative.RNAppsFlyerConstants.*;
import static com.appsflyer.reactnative.RNAppsFlyerConstants.afOnDeepLinking;

public class RudderIntegrationAppsflyerReactNativeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Application application;

    public RudderIntegrationAppsflyerReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.application = (Application) reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "RudderIntegrationAppsflyerReactNative";
    }

    @ReactMethod
    public void setup(String devKey, boolean isDebug, boolean onInstallConversionDataListener, boolean onDeepLinkListener, String appleAppId) {
        AppsFlyerLib instance = AppsFlyerLib.getInstance();
        instance.setDebugLog(isDebug);
        instance.init(devKey, (onInstallConversionDataListener == true) ? registerConversionListener() : null, application.getApplicationContext());
        if (onDeepLinkListener) {
            instance.subscribeForDeepLink(registerDeepLinkListener());
        }
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            // register for lifecycle with Activity (automatically fetching deeplink from Activity if present)
            instance.start(currentActivity, devKey);
        } else {
            // register for lifecycle with Application (cannot fetch deeplink without access to the Activity,
            // also sending first session manually)
            instance.logEvent(application, null, null);
            instance.start(application, devKey);
        }
        RNRudderAnalytics.addIntegration(AppsFlyerIntegrationFactory.FACTORY);
    }

    @ReactMethod
    public void getAppsFlyerId(Promise promise) {
        String appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(reactContext);
        promise.resolve(appsFlyerId);
    }

    private DeepLinkListener registerDeepLinkListener() {
        return new DeepLinkListener() {
            @Override
            public void onDeepLinking(@NonNull DeepLinkResult deepLinkResult) {
                JSONObject deepLinkObj = new JSONObject();
                DeepLinkResult.Error dlError = deepLinkResult.getError();
                try {
                    deepLinkObj.put("deepLinkStatus", deepLinkResult.getStatus());
                    deepLinkObj.put("status", afSuccess);
                    deepLinkObj.put("type", afOnDeepLinking);

                    if (dlError != null && deepLinkResult.getStatus() == DeepLinkResult.Status.ERROR) {
                        deepLinkObj.put("status", afFailure);
                        deepLinkObj.put("data", dlError.toString());
                        deepLinkObj.put("isDeferred", false);
                    } else if (deepLinkResult.getStatus() == DeepLinkResult.Status.FOUND) {
                        deepLinkObj.put("data", deepLinkResult.getDeepLink().getClickEvent());
                        deepLinkObj.put("isDeferred", deepLinkResult.getDeepLink().isDeferred());
                    } else {
                        deepLinkObj.put("data", "deep link not found");
                        deepLinkObj.put("isDeferred", false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    sendEvent(reactContext, afOnDeepLinking, deepLinkObj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private AppsFlyerConversionListener registerConversionListener() {
        return new AppsFlyerConversionListener() {

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                handleSuccess(afOnAppOpenAttribution, null, attributionData);
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                handleError(afOnAttributionFailure, errorMessage);
            }

            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                handleSuccess(afOnInstallConversionDataLoaded, conversionData, null);
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                handleError(afOnInstallConversionFailure, errorMessage);
            }
        };
    }

    private void handleSuccess(String eventType, Map<String, Object> conversionData, Map<String, String> attributionData) {
        JSONObject obj = new JSONObject();

        try {
            JSONObject data = new JSONObject(conversionData == null ? attributionData : conversionData);
            obj.put("status", afSuccess);
            obj.put("type", eventType);
            obj.put("data", data);
            if (eventType.equals(afOnInstallConversionDataLoaded)) {
                sendEvent(reactContext, afOnInstallConversionDataLoaded, obj.toString());
            } else if (eventType.equals(afOnAppOpenAttribution)) {
                sendEvent(reactContext, afOnAppOpenAttribution, obj.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleError(String eventType, String errorMessage) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("status", afFailure);
            obj.put("type", eventType);
            obj.put("data", errorMessage);
            sendEvent(reactContext, afOnInstallConversionDataLoaded, obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           Object params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void logLocation(double longitude, double latitude, Callback successCallback) {
        AppsFlyerLib.getInstance().logLocation(reactContext, latitude, longitude);
        successCallback.invoke(SUCCESS);
    }

    @ReactMethod
    public void setUserEmails(ReadableMap _options,
                              Callback successCallback,
                              Callback errorCallback) {

        JSONObject options = RNUtil.readableMapToJson(_options);

        int emailsCryptType = options.optInt(afEmailsCryptType, 0);
        JSONArray emailsJSON = options.optJSONArray(afEmails);

        if (emailsJSON.length() == 0) {
            errorCallback.invoke(new Exception(EMPTY_OR_CORRUPTED_LIST).getMessage());
            return;
        }

        EmailsCryptType type = EmailsCryptType.NONE; // default type

        for (EmailsCryptType _type : EmailsCryptType.values()) {
            if (_type.getValue() == emailsCryptType) {
                type = _type;
                break;
            }
        }

        String[] emailsList = new String[emailsJSON.length()];
        try {
            for (int i = 0; i < emailsJSON.length(); i++) {
                emailsList[i] = emailsJSON.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            errorCallback.invoke(new Exception(EMPTY_OR_CORRUPTED_LIST).getMessage());
            return;
        }

        AppsFlyerLib.getInstance().setUserEmails(type, emailsList);
        successCallback.invoke(SUCCESS);
    }

    @ReactMethod
    public void setAdditionalData(ReadableMap additionalData, Callback callback) {
        Map<String, Object> data = null;
        try {
            data = RNUtil.toMap(additionalData);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (data == null) { // in case of no values
            data = new HashMap<>();
        }

        HashMap<String, Object> copyData = new HashMap<>(data);
        AppsFlyerLib.getInstance().setAdditionalData(copyData);
        callback.invoke(SUCCESS);
    }

    @ReactMethod
    public void getAppsFlyerUID(Callback callback) {
        String appId = AppsFlyerLib.getInstance().getAppsFlyerUID(reactContext);
        callback.invoke(null, appId);
    }

    @ReactMethod
    public void updateServerUninstallToken(final String token, Callback callback) {
        AppsFlyerLib.getInstance().updateServerUninstallToken(reactContext, token);
        if (callback != null) {
            callback.invoke(SUCCESS);
        }
    }

    @ReactMethod
    public void setCustomerUserId(final String userId, Callback callback) {
        AppsFlyerLib.getInstance().setCustomerUserId(userId);
        callback.invoke(SUCCESS);
    }


    @ReactMethod
    public void stop(boolean isStopped, Callback callback) {
        AppsFlyerLib.getInstance().stop(isStopped, reactContext);
        callback.invoke(SUCCESS);
    }

    @ReactMethod
    public void setCollectIMEI(boolean isCollect, Callback callback) {
        AppsFlyerLib.getInstance().setCollectIMEI(isCollect);
        if (callback != null) {
            callback.invoke(SUCCESS);
        }
    }

    @ReactMethod
    public void setCollectAndroidID(boolean isCollect, Callback callback) {
        AppsFlyerLib.getInstance().setCollectAndroidID(isCollect);
        if (callback != null) {
            callback.invoke(SUCCESS);
        }
    }

    @ReactMethod
    public void setAppInviteOneLinkID(final String oneLinkID, Callback callback) {
        AppsFlyerLib.getInstance().setAppInviteOneLink(oneLinkID);
        callback.invoke(SUCCESS);
    }

    @ReactMethod
    public void generateInviteLink(ReadableMap args, final Callback successCallback, final Callback errorCallback) {

        String channel = null;
        String campaign = null;
        String referrerName = null;
        String referrerImageUrl = null;
        String customerID = null;
        String baseDeepLink = null;
        String brandDomain = null;

        LinkGenerator linkGenerator = ShareInviteHelper.generateInviteUrl(reactContext);

        try {

            JSONObject options = RNUtil.readableMapToJson(args);

            channel = options.optString(INVITE_CHANNEL, "");
            campaign = options.optString(INVITE_CAMPAIGN, "");
            referrerName = options.optString(INVITE_REFERRER, "");
            referrerImageUrl = options.optString(INVITE_IMAGEURL, "");
            customerID = options.optString(INVITE_CUSTOMERID, "");
            baseDeepLink = options.optString(INVITE_DEEPLINK, "");
            brandDomain = options.optString(INVITE_BRAND_DOMAIN, "");

            if (channel != null && channel != "") {
                linkGenerator.setChannel(channel);
            }
            if (campaign != null && campaign != "") {
                linkGenerator.setCampaign(campaign);
            }
            if (referrerName != null && referrerName != "") {
                linkGenerator.setReferrerName(referrerName);
            }
            if (referrerImageUrl != null && referrerImageUrl != "") {
                linkGenerator.setReferrerImageURL(referrerImageUrl);
            }
            if (customerID != null && customerID != "") {
                linkGenerator.setReferrerCustomerId(customerID);
            }
            if (baseDeepLink != null && baseDeepLink != "") {
                linkGenerator.setBaseDeeplink(baseDeepLink);
            }
            if (brandDomain != null && brandDomain != "") {
                linkGenerator.setBrandDomain(brandDomain);
            }


            if (options.length() > 1 && !options.get("userParams").equals("")) {

                JSONObject jsonCustomValues = options.getJSONObject("userParams");

                Iterator<?> keys = jsonCustomValues.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Object keyvalue = jsonCustomValues.get(key);
                    linkGenerator.addParameter(key, keyvalue.toString());
                }
            }

        } catch (JSONException e) {

        }

        CreateOneLinkHttpTask.ResponseListener listener = new CreateOneLinkHttpTask.ResponseListener() {
            @Override
            public void onResponse(final String oneLinkUrl) {
                successCallback.invoke(oneLinkUrl);
            }

            @Override
            public void onResponseError(final String error) {
                errorCallback.invoke(error);
            }
        };

        linkGenerator.generateLink(reactContext, listener);

    }

    @ReactMethod
    public void logCrossPromotionImpression(final String appId, final String campaign, ReadableMap params) {
        try {
            Map<String, Object> temp = RNUtil.toMap(params);
            Map<String, String> data = null;
            data = (Map) temp;
            CrossPromotionHelper.logCrossPromoteImpression(reactContext, appId, campaign, data);
        } catch (Exception e) {
            CrossPromotionHelper.logCrossPromoteImpression(reactContext, appId, campaign);
        }
    }

    @ReactMethod
    public void logCrossPromotionAndOpenStore(final String appId, final String campaign, ReadableMap params) {
        Map<String, String> data = null;
        try {
            Map<String, Object> temp = RNUtil.toMap(params);
            data = (Map) temp;
        } catch (Exception e) {
        }
        CrossPromotionHelper.logAndOpenStore(reactContext, appId, campaign, data);
    }

    @ReactMethod
    public void setCurrencyCode(final String currencyCode, Callback callback) {
        AppsFlyerLib.getInstance().setCurrencyCode(currencyCode);
        callback.invoke(SUCCESS);
    }

    @ReactMethod
    public void anonymizeUser(boolean b, Callback callback) {
        AppsFlyerLib.getInstance().anonymizeUser(b);
        callback.invoke(SUCCESS);
    }

    @ReactMethod
    public void setOneLinkCustomDomains(ReadableArray domainsArray, Callback successCallback, Callback errorCallback) {
        if (domainsArray.size() <= 0) {
            errorCallback.invoke(EMPTY_OR_CORRUPTED_LIST);
            return;
        }

//        ArrayList<Object> domainsList = domainsArray.toArrayList();
        List<Object> domainsList = RNUtil.toList(domainsArray);
        try {
            String[] domains = domainsList.toArray(new String[domainsList.size()]);
            AppsFlyerLib.getInstance().setOneLinkCustomDomain(domains);
            successCallback.invoke(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(EMPTY_OR_CORRUPTED_LIST);
        }
    }

    @ReactMethod
    public void setResolveDeepLinkURLs(ReadableArray urlsArray, Callback successCallback, Callback errorCallback) {
        if (urlsArray.size() <= 0) {
            errorCallback.invoke(EMPTY_OR_CORRUPTED_LIST);
            return;
        }

//        ArrayList<Object> urlsList = urlsArray.toArrayList();
        List<Object> urlsList = RNUtil.toList(urlsArray);
        try {
            String[] urls = urlsList.toArray(new String[urlsList.size()]);
            AppsFlyerLib.getInstance().setResolveDeepLinkURLs(urls);
            successCallback.invoke(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(EMPTY_OR_CORRUPTED_LIST);
        }
    }

    @ReactMethod
    public void performOnAppAttribution(String urlString, Callback successCallback, Callback errorCallback) {
        try {
            URI uri = URI.create(urlString);
            AppsFlyerLib.getInstance().performOnAppAttribution(reactContext, uri);
            successCallback.invoke(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(INVALID_URI);
        }
    }

    @ReactMethod
    public void disableAdvertisingIdentifier(Boolean isDisabled) {
        AppsFlyerLib.getInstance().setDisableAdvertisingIdentifiers(isDisabled);
    }

    @ReactMethod
    public void validateAndLogInAppPurchase(ReadableMap purchaseInfo, Callback successCallback, Callback errorCallback) {
        String publicKey = "";
        String signature = "";
        String purchaseData = "";
        String price = "";
        String currency = "";
        Map<String, String> additionalParameters = null;
        JSONObject additionalParametersJson;

        try {
            purchaseInfo.hasKey(ADDITIONAL_PARAMETERS);
            JSONObject purchaseJson = RNUtil.readableMapToJson(purchaseInfo);

            publicKey = purchaseJson.optString(PUBLIC_KEY, "");
            signature = purchaseJson.optString(SIGNATURE, "");
            purchaseData = purchaseJson.optString(PURCHASE_DATA, "");
            price = purchaseJson.optString(PRICE, "");
            currency = purchaseJson.optString(CURRENCY, "");
            if (purchaseInfo.hasKey(ADDITIONAL_PARAMETERS)) {
                additionalParametersJson = purchaseJson.optJSONObject(ADDITIONAL_PARAMETERS);
                additionalParameters = RNUtil.jsonObjectToMap(additionalParametersJson);
            }

            if (publicKey == "" || signature == "" || purchaseData == "" || price == "" || currency == "") {
                errorCallback.invoke(NO_PARAMETERS_ERROR);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e);
            return;
        }
        initInAppPurchaseValidatorListener(successCallback, errorCallback);
        AppsFlyerLib.getInstance().validateAndLogInAppPurchase(reactContext, publicKey, signature, purchaseData, price, currency, additionalParameters);

    }

    @ReactMethod
    public void initInAppPurchaseValidatorListener(final Callback successCallback, final Callback errorCallback) {
        AppsFlyerLib.getInstance().registerValidatorListener(reactContext, new AppsFlyerInAppPurchaseValidatorListener() {
            @Override
            public void onValidateInApp() {
                successCallback.invoke(VALIDATE_SUCCESS);

            }

            @Override
            public void onValidateInAppFailure(String error) {
                errorCallback.invoke(VALIDATE_FAILED + error);

            }
        });
    }

    @ReactMethod
    public void sendPushNotificationData(ReadableMap pushPayload) {
        JSONObject payload = RNUtil.readableMapToJson(pushPayload);
        if (payload == null) {
            Log.d("AppsFlyer", "PushNotification payload is null");
            return;
        }
        Bundle bundle = null;
        try {
            bundle = RNUtil.jsonToBundle(payload);
        } catch (JSONException e) {
            Log.d("AppsFlyer", "Can't parse pushPayload to bundle");
            e.printStackTrace();
            return;
        }
        Intent intent = getCurrentActivity().getIntent();
        intent.putExtras(bundle);
        Activity activity = getCurrentActivity();
        activity.setIntent(intent);

        AppsFlyerLib.getInstance().sendPushNotificationData(activity);
    }

    @ReactMethod
    public void setHost(String hostPrefix, String hostName, Callback successCallback) {
        AppsFlyerLib.getInstance().setHost(hostPrefix, hostName);
        successCallback.invoke(SUCCESS);
    }

    @ReactMethod
    public void addPushNotificationDeepLinkPath(ReadableArray path, Callback successCallback, Callback errorCallback) {
        if (path.size() <= 0) {
            errorCallback.invoke(EMPTY_OR_CORRUPTED_LIST);
            return;
        }
//        ArrayList<Object> pathList = path.toArrayList();
        List<Object> pathList = RNUtil.toList(path);
        try {
            String[] params = pathList.toArray(new String[pathList.size()]);
            AppsFlyerLib.getInstance().addPushNotificationDeepLinkPath(params);
            successCallback.invoke(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e);
        }
    }


    @ReactMethod
    public void setSharingFilterForPartners(ReadableArray partnersArray) {
        List<Object> partnersList = RNUtil.toList(partnersArray);
        if(partnersList == null) {
          AppsFlyerLib.getInstance().setSharingFilterForPartners(null);
        } else{
          try {
              String[] partners = partnersList.toArray(new String[partnersList.size()]);
              AppsFlyerLib.getInstance().setSharingFilterForPartners(partners);
          } catch (Exception e) {
              e.printStackTrace();
          }  
        }
    }

    @ReactMethod
    public void setPartnerData(String partnerId, ReadableMap partnerData) {
        Map partnerDataAsMap = RNUtil.toMap(partnerData);
        AppsFlyerLib.getInstance().setPartnerData(partnerId, partnerDataAsMap);
    }



}