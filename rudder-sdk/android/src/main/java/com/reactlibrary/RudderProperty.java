package com.reactlibrary;

import java.util.HashMap;
import java.util.Map;

public class RudderProperty {
    private Map<String, Object> map = new HashMap<>();

    Map<String, Object> getMap() {
        return map;
    }

    public boolean hasProperty(String key) {
        return map.containsKey(key);
    }

    public Object getProperty(String key) {
        return map.containsKey(key) ? map.get(key) : null;
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public RudderProperty putValue(String key, Object value) {
        if (value instanceof RudderProperty) {
            this.map.put(key, ((RudderProperty) value).getMap());
        } else {
            map.put(key, value);
        }
        return this;
    }

    public RudderProperty putValue(Map<String, Object> map) {
        if (map != null) this.map.putAll(map);
        return this;
    }

    public void putRevenue(double revenue) {
        map.put("revenue", revenue);
    }

    public void putCurrency(String currency) {
        map.put("currency", currency);
    }
}
