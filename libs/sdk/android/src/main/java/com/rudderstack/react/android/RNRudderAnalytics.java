package com.rudderstack.react.android;

import androidx.annotation.Nullable;

import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;

import java.util.ArrayList;
import java.util.List;

public class RNRudderAnalytics {
    static List<RudderIntegration.Factory> integrationList;
    static RudderConfig.DBEncryption rsDBEncryption = null;

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

    public static void setDBEncryption(RudderConfig.DBEncryption dbEncryption) {
        rsDBEncryption = dbEncryption;
    }

    @Nullable
    public static RudderConfig.DBEncryption getDBEncryption() {
        return rsDBEncryption;
    }
}
