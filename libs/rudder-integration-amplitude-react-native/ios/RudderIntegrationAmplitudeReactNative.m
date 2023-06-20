#import "RudderIntegrationAmplitudeReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Amplitude/RudderAmplitudeFactory.h>

@implementation RudderIntegrationAmplitudeReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderAmplitudeFactory instance]];
    resolve(nil);
}

@end
