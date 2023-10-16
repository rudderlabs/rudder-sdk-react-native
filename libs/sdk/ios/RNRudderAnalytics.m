//
//  RNRudderAnalytics.m
//  CocoaAsyncSocket
//
//  Created by Dhawal on 28/07/20.
//

#import "RNRudderAnalytics.h"

NSMutableArray* integrationList;
static RSDBEncryption* rsDBEncryption = nil;

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
@end
