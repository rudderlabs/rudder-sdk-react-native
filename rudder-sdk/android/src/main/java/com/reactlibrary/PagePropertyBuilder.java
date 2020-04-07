package com.reactlibrary;

import android.text.TextUtils;

/*
 * Builder for event type "page"
 * - `url` is a mandatory field for page events
 * */
public class PagePropertyBuilder extends RudderPropertyBuilder {
    private String title;

    public PagePropertyBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    private String url;

    public PagePropertyBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    private String path;

    public PagePropertyBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    private String referrer;

    public PagePropertyBuilder setReferrer(String referrer) {
        this.referrer = referrer;
        return this;
    }

    private String search;

    public PagePropertyBuilder setSearch(String search) {
        this.search = search;
        return this;
    }

    private String keywords;

    public PagePropertyBuilder setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    @Override
    public RudderProperty build() {
        RudderProperty property = new RudderProperty();
        if (TextUtils.isEmpty(url)) {
            RudderLogger.logError("url can not be null or empty");
        } else {
            property.put("title", this.title);
            property.put("url", this.url);
            property.put("path", this.path);
            property.put("referrer", this.referrer);
            property.put("search", this.search);
            property.put("keywords", this.keywords);
        }
        return property;
    }
}
