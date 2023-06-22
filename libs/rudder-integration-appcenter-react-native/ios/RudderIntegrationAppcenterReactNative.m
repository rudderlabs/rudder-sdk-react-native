#import "RudderIntegrationAppcenterReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-AppCenter/RudderAppCenterFactory.h>

@import AppCenterAnalytics;
@implementation RudderIntegrationAppcenterReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderAppCenterFactory instance]];
    resolve(nil);
}

RCT_EXPORT_METHOD(enableAnalytics)
{
    [MSACAnalytics setEnabled:true];
}

RCT_EXPORT_METHOD(disableAnalytics)
{
    [MSACAnalytics setEnabled:false];
}

@end
