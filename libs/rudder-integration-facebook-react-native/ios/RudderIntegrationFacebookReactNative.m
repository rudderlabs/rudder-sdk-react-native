#import "RudderIntegrationFacebookReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#if __has_include(<Rudder-Facebook/RudderFacebookFactory.h>)
#import <Rudder-Facebook/RudderFacebookFactory.h>
#else
@import Rudder_Facebook;
#endif

@implementation RudderIntegrationFacebookReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [RNRudderAnalytics addIntegration:[RudderFacebookFactory instance]];
    resolve(nil);
}

@end
