package com.rudderstack.react.android;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderTraits;
import com.rudderstack.android.sdk.core.RudderOption;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public class Utility {
    public static final String EXTERNAL_ID = "externalId";
    public static final String EXTERNAL_IDS = "externalIds";
    public static final String INTEGRATIONS = "integrations";

    public static Map<String, Object> convertReadableMapToMap(ReadableMap readableMap) {
        if (readableMap == null) return null;

        Map<String, Object> map = new HashMap<>();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            switch (readableMap.getType(key)) {
                case Null:
                    map.put(key, JSONObject.NULL);
                    break;
                case Boolean:
                    map.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    map.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    map.put(key, readableMap.getString(key));
                    break;
                case Map:
                    map.put(key, convertReadableMapToMap(readableMap.getMap(key)));
                    break;
                case Array:
                    map.put(key, convertReadableArrayToList(readableMap.getArray(key)));
                    break;
            }
        }
        return map;
    }

    public static List<Object> convertReadableArrayToList(ReadableArray readableArray) {
        if (readableArray == null) return null;

        ArrayList<Object> array = new ArrayList<>();
        for (int i = 0; i < readableArray.size(); i++) {
            switch (readableArray.getType(i)) {
                case Null:
                    break;
                case Boolean:
                    array.add(readableArray.getBoolean(i));
                    break;
                case Number:
                    array.add(readableArray.getDouble(i));
                    break;
                case String:
                    array.add(readableArray.getString(i));
                    break;
                case Map:
                    array.add(convertReadableMapToMap(readableArray.getMap(i)));
                    break;
                case Array:
                    array.add(convertReadableArrayToList(readableArray.getArray(i)));
                    break;
            }
        }
        return array;
    }

    public static RudderTraits convertReadableMapToTraits(ReadableMap readableMap) {
        if (readableMap == null) return null;
        RudderTraits traits = new RudderTraits();
        Map<String, Object> map = convertReadableMapToMap(readableMap);
        for (String key : map.keySet()) {
            traits.put(key, map.get(key));
        }
        return traits;
    }

    static RudderOption convertReadableMapToOptions(ReadableMap readableMap) {
        if (readableMap == null) return null;
        RudderOption options = new RudderOption();
        try {
            Map<String, Object> optionsMap = convertReadableMapToMap(readableMap);
            removeExternalIdsIfExternalIdExists(optionsMap);

            for (String key : optionsMap.keySet()) {
                switch (key) {
                    case EXTERNAL_ID -> setExternalId(EXTERNAL_ID, readableMap, options);
                    case EXTERNAL_IDS -> setExternalId(EXTERNAL_IDS, readableMap, options);
                    case INTEGRATIONS -> setIntegrations(optionsMap, options);
                    default -> setCustomContext(key, optionsMap, options);
                }
            }
        } catch (Exception e) {
            RudderLogger.logError("Exception occurred while handling options: " + e.getMessage());
        }
        return options;
    }

    // For legacy reason we are still supporting "externalIds". First priority is given to "externalId".
    private static void removeExternalIdsIfExternalIdExists(Map<String, Object> optionsMap) {
        if (optionsMap != null) {
            if (optionsMap.containsKey(EXTERNAL_ID) && optionsMap.containsKey(EXTERNAL_IDS)) {
                optionsMap.remove(EXTERNAL_IDS);
            }
        }
    }

    private static void setExternalId(String key, ReadableMap readableMap, RudderOption options) {
        if (readableMap.getType(key) == ReadableType.Array) {
            List<Object> externalIdsList = convertReadableArrayToList(readableMap.getArray(key));
            if (!isEmpty(externalIdsList)) {
                for (int i = 0; i < externalIdsList.size(); i++) {
                    Map<String, Object> externalId = (Map<String, Object>) externalIdsList.get(i);
                    if (isExternalIdValid(externalId)) {
                        options.putExternalId(getString(externalId.get("type")), getString(externalId.get("id")));
                    }
                }

            }
        }
    }

    private static void setIntegrations(Map<String, Object> optionsMap, RudderOption options) {
        if (optionsMap.get(INTEGRATIONS) instanceof Map) {
            Map<String, Object> integrationsMap = (Map<String, Object>) optionsMap.get(INTEGRATIONS);
            if (!isEmpty(integrationsMap)) {
                for (String key : integrationsMap.keySet()) {
                    Object value = integrationsMap.get(key);
                    if (value instanceof Boolean) {
                        options.putIntegration(key, getBoolean(value));
                    }
                }
            }
        }
    }

    private static void setCustomContext(String key, Map<String, Object> optionsMap, RudderOption options) {
        if (optionsMap.get(key) instanceof Map && !isEmpty(optionsMap.get(key))) {
            Map<String, Object> optionsMapContext = (Map<String, Object>) optionsMap.get(key);
            Map customContext = new HashMap<String, Object>();
            for (String contextKey : optionsMapContext.keySet()) {
                customContext.put(contextKey, optionsMapContext.get(contextKey));
            }
            options.putCustomContext(key, customContext);
        }
    }

    private static boolean isExternalIdValid(Map<String, Object> externalId) {
        return (!isEmpty(externalId) && externalId.containsKey("type") && externalId.containsKey("id"));
    }

    public static String getString(Object obj) {
        if (obj == null) return "";
        return obj.toString();
    }

    public static boolean getBoolean(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else {
            return false;
        }
    }

    public static WritableMap convertJSONObjectToWriteAbleMap(JSONObject json) throws JSONException {
        WritableMap map = new WritableNativeMap();

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                map.putMap(key, convertJSONObjectToWriteAbleMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.putArray(key, convertJsonToWriteAbleArray((JSONArray) value));
            } else if (value instanceof Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String) {
                map.putString(key, (String) value);
            } else {
                map.putString(key, value.toString());
            }
        }
        return map;
    }

    public static WritableArray convertJsonToWriteAbleArray(JSONArray jsonArray) throws JSONException {
        WritableArray array = new WritableNativeArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                array.pushMap(convertJSONObjectToWriteAbleMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                array.pushArray(convertJsonToWriteAbleArray((JSONArray) value));
            } else if (value instanceof Boolean) {
                array.pushBoolean((Boolean) value);
            } else if (value instanceof Integer) {
                array.pushInt((Integer) value);
            } else if (value instanceof Double) {
                array.pushDouble((Double) value);
            } else if (value instanceof String) {
                array.pushString((String) value);
            } else {
                array.pushString(value.toString());
            }
        }
        return array;
    }

    @Nonnull
    public static Object getValueFromMap(Map<String, Object> map, String key, Object defaultValue) {
        if (map == null || key == null) return defaultValue;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return defaultValue;
    }

    public static boolean isEmpty(Object value) {
        if (value == null) return true;
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        }
        if (value instanceof List) {
            return ((List<?>) value).isEmpty();
        }
        return false;
    }
}
