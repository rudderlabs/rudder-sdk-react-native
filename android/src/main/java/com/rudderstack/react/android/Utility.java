package com.rudderstack.react.android;

import android.icu.text.RelativeDateTimeFormatter;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.rudderstack.android.sdk.core.RudderTraits;
import com.rudderstack.android.sdk.core.RudderOption;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.facebook.react.bridge.ReadableType.Null;

class Utility {
    static Map<String, Object> convertReadableMapToMap(ReadableMap readableMap) {
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

    private static List<Object> convertReadableArrayToList(ReadableArray readableArray) {
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

    static RudderTraits convertReadableMapToTraits(ReadableMap readableMap) {
        if (readableMap == null) return null;
        RudderTraits traits = new RudderTraits();
        Map<String, Object> map = convertReadableMapToMap(readableMap);
        for (String key : map.keySet()) {
            traits.put(key, map.get(key));
        }
        return traits;
    }

    static RudderOption convertReadableMapToOptions(ReadableMap readableMap)
    {
        if (readableMap == null) return null;
        RudderOption options = new RudderOption();
        Map<String, Object> optionsMap = convertReadableMapToMap(readableMap);
        if(readableMap.hasKey("externalIds"))
        {
        List<Object> externalIdsList = convertReadableArrayToList(readableMap.getArray("externalIds"));
        for(int i=0;i<externalIdsList.size();i++)
        {
            Map<String,Object> externalId = (Map<String,Object>) externalIdsList.get(i);
            options.putExternalId((String)externalId.get("type"),(String)externalId.get("id"));
        }
       }
       if(optionsMap.containsKey("integrations"))
       {
        Map<String,Object> integrationsMap = (Map<String,Object>)optionsMap.get("integrations");
        for (String key : integrationsMap.keySet()) {
            options.putIntegration(key, (boolean)integrationsMap.get(key));
        }
        }
        return options;
    }
}
