//
//  RNRudderAnalytics.m
//  CocoaAsyncSocket
//
//  Created by Dhawal on 28/07/20.
//

#import "RNRudderAnalytics.h"

NSMutableArray* integrationList;

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

@end
