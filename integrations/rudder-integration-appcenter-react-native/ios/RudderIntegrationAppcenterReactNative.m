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
    NSLog(@"Enabling Analytics Module");
    [MSACAnalytics setEnabled:true];
}

RCT_EXPORT_METHOD(disableAnalytics)
{
    NSLog(@"Disabling Analytics Module");
    [MSACAnalytics setEnabled:false];
}

RCT_EXPORT_METHOD (isEnabled)
{
    NSLog(@"Status Of Analytics Module is %@",[MSACAnalytics isEnabled]);
}
@end
