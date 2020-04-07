package com.reactlibrary;

import java.util.Map;

/*
 * builder for RudderElement (alias RudderEvent)
 * */
public class RudderMessageBuilder {
    private String eventName = null;

    public RudderMessageBuilder setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    private String userId = null;

    public RudderMessageBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    private RudderProperty property;

    public RudderMessageBuilder setProperty(RudderProperty property) {
        this.property = property;
        return this;
    }

    public RudderMessageBuilder setProperty(RudderPropertyBuilder builder) {
        this.property = builder.build();
        return this;
    }

    public RudderMessageBuilder setProperty(Map<String, Object> map) {
        if (this.property == null) property = new RudderProperty();
        property.putValue(map);
        return this;
    }

    private RudderUserProperty userProperty;

    public RudderMessageBuilder setUserProperty(RudderUserProperty userProperty) {
        this.userProperty = userProperty;
        return this;
    }

    public RudderMessageBuilder setUserProperty(Map<String, Object> map) {
        this.userProperty = new RudderUserProperty();
        userProperty.putValue(map);
        return this;
    }

    // TODO:  need to figure out to use it as integrations
    private RudderOption option;

    public RudderMessageBuilder setRudderOption(RudderOption option) {
        this.option = option;
        return this;
    }

    public RudderMessage build() {
        RudderMessage event = new RudderMessage();
        if (this.userId != null) event.setUserId(this.userId);
        if (this.eventName != null) event.setEventName(this.eventName);
        if (this.property != null) event.setProperty(this.property);
        if (this.userProperty != null) event.setUserProperty(this.userProperty);
        return event;
    }
}
