#import "RudderIntegrationFirebaseReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Firebase/RudderFirebaseFactory.h>

@implementation RudderIntegrationFirebaseReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderFirebaseFactory instance]];
    resolve(nil);
}

@end
