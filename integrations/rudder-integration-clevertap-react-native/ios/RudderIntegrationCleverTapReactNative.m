#import "RudderIntegrationCleverTapReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-CleverTap/RudderCleverTapFactory.h>

@implementation RudderIntegrationCleverTapReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup)
{
    [RNRudderAnalytics addIntegration:[RudderCleverTapFactory instance]];
}

@end
