#import "RudderIntegrationAppcenterReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Appcenter/RudderAppcenterFactory.h>

@implementation RudderIntegrationAppcenterReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup)
{
    [RNRudderAnalytics addIntegration:[RudderAppcenterFactory instance]];
}

@end
