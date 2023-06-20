#import "RudderIntegrationSingularReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Singular/RudderSingularFactory.h>

@implementation RudderIntegrationSingularReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderSingularFactory instance]];
    resolve(nil);
}

@end
