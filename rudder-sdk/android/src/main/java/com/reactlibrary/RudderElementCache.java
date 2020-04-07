package com.reactlibrary;

import android.app.Application;

/*
* RudderContext is populated once and cached through out the lifecycle
* */
class RudderElementCache {
    private static RudderContext cachedContext;

    private RudderElementCache() {
        // stop instantiating
    }

    static void initiate(Application application) {
        if (cachedContext == null) {
            cachedContext = new RudderContext(application);
        }
    }

    static RudderContext getCachedContext() {
        return cachedContext;
    }
}

