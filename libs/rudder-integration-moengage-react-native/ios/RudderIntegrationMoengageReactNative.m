#import "RudderIntegrationMoengageReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Moengage/RudderMoengageFactory.h>

@implementation RudderIntegrationMoengageReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderMoengageFactory instance]];
    resolve(nil);
}

@end
