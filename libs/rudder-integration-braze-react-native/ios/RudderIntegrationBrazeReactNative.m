#import "RudderIntegrationBrazeReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Braze/RudderBrazeFactory.h>

@implementation RudderIntegrationBrazeReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderBrazeFactory instance]];
    resolve(nil);
}

@end
