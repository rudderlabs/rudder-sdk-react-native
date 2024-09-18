#import "RudderIntegrationFirebaseReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Firebase/RudderFirebaseFactory.h>)
#import <Rudder-Firebase/RudderFirebaseFactory.h>
#else
@import Rudder_Firebase;
#endif

@implementation RudderIntegrationFirebaseReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderFirebaseFactory instance]];
    resolve(nil);
}

@end
