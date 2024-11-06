package com.rudderstack.react.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.consent.RudderConsentFilter;

import java.util.ArrayList;
import java.util.List;

public class RNRudderAnalytics {
    static List<RudderIntegration.Factory> integrationList;
    static RudderConfig.DBEncryption rsDBEncryption = null;
    private static RudderConsentFilter rsConsentFilter;

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

    public static void setConsentFilterPlugin(@NonNull RudderConsentFilter consentFilter) {
        rsConsentFilter = consentFilter;
    }

    @Nullable
    public static RudderConsentFilter getConsentFilterPlugin() {
        return rsConsentFilter;
    }
}
