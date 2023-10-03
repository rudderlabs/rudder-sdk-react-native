#import "RudderIntegrationAmplitudeReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Amplitude/RudderAmplitudeFactory.h>)
#import <Rudder-Amplitude/RudderAmplitudeFactory.h>
#else
@import Rudder_Amplitude;
#endif

@implementation RudderIntegrationAmplitudeReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderAmplitudeFactory instance]];
    resolve(nil);
}

@end
