#import "RudderIntegrationFacebookReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <RudderFacebookFactory.h>

@implementation RudderIntegrationFacebookReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderFacebookFactory instance]];
    resolve(nil);
}

@end
