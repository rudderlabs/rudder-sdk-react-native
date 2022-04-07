package com.rudderstack.integration.reactnative.appsflyer;

import com.appsflyer.AppsFlyerLib;
import com.facebook.react.bridge.Promise;

import androidx.annotation.NonNull;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.rudderstack.android.integrations.appsflyer.AppsFlyerIntegrationFactory;
import com.rudderstack.react.android.RNRudderAnalytics;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RudderIntegrationAppsflyerReactNativeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final static String afSuccess = "success";
    private final static String afFailure = "failure";
    private final static String afOnAttributionFailure = "onAttributionFailure";
    private final static String afOnAppOpenAttribution = "onAppOpenAttribution";
    private final static String afOnInstallConversionFailure = "onInstallConversionFailure";
    private final static String afOnInstallConversionDataLoaded = "onInstallConversionDataLoaded";
    private final static String afOnDeepLinking = "onDeepLinking";


    public RudderIntegrationAppsflyerReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RudderIntegrationAppsflyerReactNative";
    }

    @ReactMethod
    public void setup() {
        RNRudderAnalytics.addIntegration(AppsFlyerIntegrationFactory.FACTORY);
        AppsFlyerLib instance = AppsFlyerLib.getInstance();
        instance.subscribeForDeepLink(registerDeepLinkListener());
        instance.registerConversionListener(reactContext, registerConversionListener());
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

}