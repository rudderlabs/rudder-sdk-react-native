#import "RudderIntegrationCleverTapReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-CleverTap/RudderCleverTapFactory.h>)
#import <Rudder-CleverTap/RudderCleverTapFactory.h>
#else
@import Rudder_CleverTap;
#endif

@implementation RudderIntegrationCleverTapReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderCleverTapFactory instance]];
    resolve(nil);
}

@end
