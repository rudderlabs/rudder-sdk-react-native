package com.rudderstack.android.integrations.appsflyer;

import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;
import com.rudderstack.android.sdk.core.ecomm.ECommerceEvents;
import com.rudderstack.android.sdk.core.ecomm.ECommerceParamNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppsFlyerIntegrationFactory extends RudderIntegration<AppsFlyerLib> implements AppsFlyerConversionListener {
    private static final String APPSFLYER_KEY = "AppsFlyer";
    private Boolean isNewScreenEnabled = false;

    public static RudderIntegration.Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(Object settings, RudderClient client, RudderConfig config) {
            return new AppsFlyerIntegrationFactory(settings, config);
        }

        @Override
        public String key() {
            return APPSFLYER_KEY;
        }
    };

    private AppsFlyerIntegrationFactory(Object config, RudderConfig rudderConfig) {
        Map<String, Object> destConfig = (Map<String, Object>) config;
        if (destConfig != null) {
            if (destConfig.containsKey("useRichEventName") && destConfig.get("useRichEventName") != null) {
                isNewScreenEnabled = (Boolean) destConfig.get("useRichEventName");
            }
            if (destConfig.containsKey("devKey")) {
                String appsFlyerKey = getString(destConfig.get("devKey"));
                if (!TextUtils.isEmpty(appsFlyerKey)) {
                    AppsFlyerLib.getInstance().setLogLevel(
                            rudderConfig.getLogLevel() >= RudderLogger.RudderLogLevel.DEBUG ?
                                    AFLogger.LogLevel.VERBOSE : AFLogger.LogLevel.NONE);
                    AppsFlyerLib.getInstance().start(RudderClient.getApplication());
                    System.out.println("Desu: SDK Started");
                }
            }
        }
    }

    private void processEvents(RudderMessage message) {
        String eventType = message.getType();
        String afEventName;
        Map<String, Object> afEventProps = new HashMap<>();
        if (eventType != null) {
            switch (eventType) {
                case MessageType.TRACK:
                    String eventName = message.getEventName();
                    if (eventName != null) {
                        Map<String, Object> property = message.getProperties();
                        if (property != null) {
                            switch (eventName) {
                                case ECommerceEvents.PRODUCTS_SEARCHED:
                                    if (property.containsKey(ECommerceParamNames.QUERY)) {
                                        afEventProps.put(AFInAppEventParameterName.SEARCH_STRING, property.get("query"));
                                    }
                                    afEventName = AFInAppEventType.SEARCH;
                                    break;
                                case ECommerceEvents.PRODUCT_VIEWED:
                                    if (property.containsKey(ECommerceParamNames.PRICE))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.CONTENT_VIEW;
                                    break;
                                case ECommerceEvents.PRODUCT_LIST_VIEWED:
                                    if (property.containsKey(ECommerceParamNames.CATEGORY)) {
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    }
                                    if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                                        JSONArray productsJSON = getJSONArray(property.get(ECommerceParamNames.PRODUCTS));
                                        if (productsJSON != null) {
                                            ArrayList<String> products = new ArrayList<>();
                                            for (int i = 0; i < productsJSON.length(); i++) {
                                                try {
                                                    JSONObject product = (JSONObject) productsJSON.get(i);
                                                    if (product.has("product_id")) {
                                                        products.add(getString(product.get("product_id")));
                                                    }
                                                } catch (JSONException e) {
                                                    RudderLogger.logDebug("Error while getting Products: " + productsJSON);
                                                }
                                            }
                                            afEventProps.put(AFInAppEventParameterName.CONTENT_LIST, products.toArray());
                                        }
                                    }
                                    afEventName = "af_list_view";
                                    break;
                                case ECommerceEvents.PRODUCT_ADDED_TO_WISH_LIST:
                                    if (property.containsKey(ECommerceParamNames.PRICE))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.ADD_TO_WISH_LIST;
                                    break;
                                case ECommerceEvents.PRODUCT_ADDED:
                                    if (property.containsKey(ECommerceParamNames.PRICE))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    if (property.containsKey(ECommerceParamNames.QUANTITY))
                                        afEventProps.put(AFInAppEventParameterName.QUANTITY, property.get(ECommerceParamNames.QUANTITY));
                                    afEventName = AFInAppEventType.ADD_TO_CART;
                                    break;
                                case ECommerceEvents.CHECKOUT_STARTED:
                                    if (property.containsKey(ECommerceParamNames.TOTAL))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.TOTAL));
                                    if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                                        handleProducts(property, afEventProps);
                                    }
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.INITIATED_CHECKOUT;
                                    break;
                                case ECommerceEvents.ORDER_COMPLETED:
                                    if (property.containsKey(ECommerceParamNames.TOTAL))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.TOTAL));
                                    if (property.containsKey(ECommerceParamNames.REVENUE))
                                        afEventProps.put(AFInAppEventParameterName.REVENUE, property.get(ECommerceParamNames.REVENUE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                                        handleProducts(property, afEventProps);
                                    }
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    if (property.containsKey(ECommerceParamNames.ORDER_ID)) {
                                        afEventProps.put(AFInAppEventParameterName.RECEIPT_ID, property.get(ECommerceParamNames.ORDER_ID));
                                        afEventProps.put("af_order_id", property.get(ECommerceParamNames.ORDER_ID));
                                    }
                                    afEventName = AFInAppEventType.PURCHASE;
                                    break;
                                case ECommerceEvents.PRODUCT_REMOVED:
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    afEventName = "remove_from_cart";
                                    break;
                                default:
                                    afEventName = eventName.toLowerCase().replace(" ", "_");
                            }
                        } else {
                            afEventName = eventName.toLowerCase().replace(" ", "_");
                        }
                        AppsFlyerLib.getInstance().logEvent(RudderClient.getApplication(), afEventName, afEventProps);
                    }
                    break;
                case MessageType.SCREEN:
                    String screenName;
                    Map<String, Object> properties = message.getProperties();
                    if (isNewScreenEnabled) {
                        if (!TextUtils.isEmpty(message.getEventName())) {
                            screenName = "Viewed " + message.getEventName() + " Screen";
                        } else if (properties != null &&
                                properties.containsKey("name") &&
                                !TextUtils.isEmpty((String) properties.get("name"))) {
                            screenName = "Viewed " + properties.get("name") + " Screen";
                        } else {
                            screenName = "Viewed Screen";
                        }
                    } else {
                        screenName = "screen";
                    }
                    AppsFlyerLib.getInstance().logEvent(RudderClient.getApplication(), screenName, properties);
                    break;
                case MessageType.IDENTIFY:
                    String userId = message.getUserId();
                    AppsFlyerLib.getInstance().setCustomerUserId(userId);
                    if (message.getTraits().containsKey("email")) {
                        AppsFlyerLib.getInstance().setUserEmails(getString(message.getTraits().get("email")));
                    }
                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    private void handleProducts(Map<String, Object> property, Map<String, Object> afEventProps) {
        JSONArray products = getJSONArray(property.get(ECommerceParamNames.PRODUCTS));
        if (products != null && products.length() > 0) {
            ArrayList<String> pIds = new ArrayList<>();
            ArrayList<String> pCats = new ArrayList<>();
            ArrayList<String> pQnts = new ArrayList<>();
            for (int i = 0; i < products.length(); i++) {
                try {
                    JSONObject product = (JSONObject) products.get(i);
                    if (product.has("product_id") && product.has("category") && product.has("quantity")) {
                        pIds.add(getString(product.get("product_id")));
                        pCats.add(getString(product.get("category")));
                        pQnts.add(getString(product.get("quantity")));
                    }
                } catch (JSONException e) {
                    RudderLogger.logDebug("Error while getting Products: " + products);
                }
            }
            afEventProps.put(AFInAppEventParameterName.CONTENT_ID, pIds.toArray());
            afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, pCats.toArray());
            afEventProps.put(AFInAppEventParameterName.QUANTITY, pQnts.toArray());
        }
    }

    private JSONArray getJSONArray(Object object) {
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        if (object instanceof List) {
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.addAll((Collection<?>) object);
            return new JSONArray(arrayList);
        }
        try {
            return new JSONArray((ArrayList) object);
        } catch (Exception e) {
            RudderLogger.logDebug("Error while converting the products: " + object + " to JSONArray type");
        }
        return null;
    }

    private static String getString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    @Override
    public void onConversionDataSuccess(Map<String, Object> map) {
      System.out.println("Desu: On COnversion Data Success"+map.toString());
    }

    @Override
    public void onConversionDataFail(String s) {
       System.out.println("Desu: On COnversion Data Failure"+s);
    }

    @Override
    public void onAppOpenAttribution(Map<String, String> map) {
        System.out.println("Desu: On App Open Attribution"+map.toString());
    }

    @Override
    public void onAttributionFailure(String s) {
        System.out.println("Desu: On Attribution Failure" + s);
    }

    @Override
    public void reset() {

    }

    @Override
    public void dump(RudderMessage element) {
        if (element != null) {
            this.processEvents(element);
        }
    }

    @Override
    public AppsFlyerLib getUnderlyingInstance() {
        return AppsFlyerLib.getInstance();
    }
}
