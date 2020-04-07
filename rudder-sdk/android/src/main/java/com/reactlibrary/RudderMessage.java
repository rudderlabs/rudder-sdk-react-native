package com.reactlibrary;

import com.google.gson.annotations.SerializedName;
import com.rudderlabs.android.sdk.core.util.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class RudderMessage {
    @SerializedName("messageId")
    private String messageId = String.format(Locale.US, "%d-%s", System.currentTimeMillis(), UUID.randomUUID().toString());
    @SerializedName("channel")
    private String channel = "mobile";
    @SerializedName("context")
    private RudderContext context;
    @SerializedName("type")
    private String type;
    @SerializedName("action")
    private String action;
    @SerializedName("originalTimestamp")
    private String timestamp = Utils.getTimeStamp();
    @SerializedName("anonymousId")
    private String anonymousId;
    @SerializedName("userId")
    private String userId;
    @SerializedName("event")
    private String event;
    @SerializedName("properties")
    private Map<String, Object> properties;
    @SerializedName("userProperties")
    private Map<String, Object> userProperties;
    @SerializedName("integrations")
    private Map<String, Boolean> integrations = new HashMap<>();
    @SerializedName("destinationProps")
    private Map<String, Map> destinationProps = null;

    RudderMessage() {
        context = RudderElementCache.getCachedContext();
        this.anonymousId = context.getDeviceId();
    }

    void setProperty(RudderProperty property) {
        if (property != null) this.properties = property.getMap();
    }

    void setUserProperty(RudderUserProperty userProperty) {
        this.userProperties = userProperty.getMap();
    }

    void setType(String type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEventName(String eventName) {
        this.event = eventName;
    }

    void updateTraits(RudderTraits traits) {
        this.context.updateTraits(traits);
    }

    public String getEventName() {
        return event;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    void addIntegrationProps(String integrationKey, boolean isEnabled, Map props) {
        integrations.put(integrationKey, isEnabled);

        if (isEnabled) {
            if (destinationProps == null) destinationProps = new HashMap<>();
            destinationProps.put(integrationKey, props);
        }
    }

    public String getType() {
        return type;
    }

    String getAction() {
        return action;
    }

    public Map<String, Object> getUserProperties() {
        return userProperties;
    }

    void setIntegrations(Map<String, Object> integrations) {
        if (integrations == null) {
            return;
        }
        for (String key : integrations.keySet()) {
            this.integrations.put(key, (Boolean) integrations.get(key));
        }
    }
}
