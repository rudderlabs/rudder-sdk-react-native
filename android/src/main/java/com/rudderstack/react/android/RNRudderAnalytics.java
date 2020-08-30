package com.rudderstack.react.android;

import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;

import java.util.ArrayList;
import java.util.List;

public class RNRudderAnalytics {
    static List<RudderIntegration.Factory> integrationList;

    public static void addIntegration(RudderIntegration.Factory integration) {
        if (integrationList == null) {
            integrationList = new ArrayList<>();
        }
        integrationList.add(integration);
    }

    public static RudderConfig buildWithIntegrations(RudderConfig.Builder builder) {
        if (integrationList != null) {
            builder.withFactories(integrationList);
        }
        return builder.build();
    }

    // TODO : Add support for adding callbacks
}
