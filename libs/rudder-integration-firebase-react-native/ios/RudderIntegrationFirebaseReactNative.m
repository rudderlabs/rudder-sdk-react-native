#import "RudderIntegrationFirebaseReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Firebase/RudderFirebaseFactory.h>

@implementation RudderIntegrationFirebaseReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup)
{
    [RNRudderAnalytics addIntegration:[RudderFirebaseFactory instance]];
}

@end
