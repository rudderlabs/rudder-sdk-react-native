#import "RudderIntegrationAppcenterReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-AppCenter/RudderAppcenterFactory.h>

@import AppCenterAnalytics;
@implementation RudderIntegrationAppcenterReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup)
{
    [RNRudderAnalytics addIntegration:[RudderAppCenterFactory instance]];
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
