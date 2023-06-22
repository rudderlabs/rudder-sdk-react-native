#import "RudderIntegrationCleverTapReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-CleverTap/RudderCleverTapFactory.h>

@implementation RudderIntegrationCleverTapReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderCleverTapFactory instance]];
    resolve(nil);
}

@end
