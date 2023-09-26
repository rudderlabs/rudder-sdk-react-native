#import "RudderIntegrationMoengageReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Moengage/RudderMoengageFactory.h>)
#import <Rudder-Moengage/RudderMoengageFactory.h>
#else
@import Rudder_Moengage;
#endif

@implementation RudderIntegrationMoengageReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderMoengageFactory instance]];
    resolve(nil);
}

@end
