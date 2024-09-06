#import "RudderPluginKetchConsentFilterReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>


@implementation RudderPluginKetchConsentFilterReactNative

RCT_EXPORT_MODULE()
    
RudderKetchConsentFilterPlugin *ketchConsentFilterPlugin = nil;
RCT_EXPORT_METHOD(startConsentFilterPlugin:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    ketchConsentFilterPlugin = [[RudderKetchConsentFilterPlugin alloc] initWithReactContext:resolve rejecter:reject];
    [ketchConsentFilterPlugin setupOneTrust];
}

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if (ketchConsentFilterPlugin != nil) {
        [ketchConsentFilterPlugin setConsentFilter];
    }
    resolve(nil);
}

@end
