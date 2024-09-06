//
//  RNRudderAnalytics.m
//  CocoaAsyncSocket
//
//  Created by Dhawal on 28/07/20.
//

#import "RNRudderAnalytics.h"

NSMutableArray* integrationList;
static RSDBEncryption* rsDBEncryption = nil;
static id<RSConsentFilter> rsConsentFilter = nil;

@implementation RNRudderAnalytics


+ (void) addIntegration:(id<RSIntegrationFactory>)integration {
    if (integrationList == nil) {
        integrationList = [[NSMutableArray alloc] init];
    }
    [integrationList addObject:integration];
}

+ (RSConfig*) buildWithIntegrations:(RSConfigBuilder*)builder {
    if (integrationList != nil) {
        for (id<RSIntegrationFactory> integration in integrationList) {
            [builder withFactory:integration];
        }
    }
    return [builder build];
}

+ (void) setDBEncryption:(RSDBEncryption *)dbEncryption {
    rsDBEncryption = dbEncryption;
}

+ (RSDBEncryption *_Nullable) getDBEncryption {
    return rsDBEncryption;
}

+ (void) setConsentFilterPlugin:(id <RSConsentFilter> _Nonnull)consentFilter {
    rsConsentFilter = consentFilter;
}

+ (id <RSConsentFilter> _Nullable) getConsentFilterPlugin {
    return rsConsentFilter;
}

@end
