#import "RudderIntegrationSingularReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Singular/RudderSingularFactory.h>)
#import <Rudder-Singular/RudderSingularFactory.h>
#else
@import Rudder_Singular;
#endif

@implementation RudderIntegrationSingularReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderSingularFactory instance]];
    resolve(nil);
}

@end
