#import "RudderIntegrationBrazeReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Braze/RudderBrazeFactory.h>)
#import <Rudder-Braze/RudderBrazeFactory.h>
#else
@import Rudder_Braze;
#endif

@implementation RudderIntegrationBrazeReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderBrazeFactory instance]];
    resolve(nil);
}

@end
