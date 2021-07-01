#import "RudderIntegrationAppsflyerReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder-Appsflyer/RudderAppsflyerFactory.h>

@implementation RudderIntegrationAppsflyerReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup)
{
    [RNRudderAnalytics addIntegration:[RudderAppsflyerFactory instance]];
}

RCT_EXPORT_METHOD(getAppsFlyerId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSString *appsflyerId = [AppsFlyerLib shared].getAppsFlyerUID;
    resolve(appsflyerId);
}

@end
