#import "RudderPluginOnetrustConsentFilterReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>


@implementation RudderPluginOnetrustConsentFilterReactNative

RCT_EXPORT_MODULE()
    
RudderOnetrustConsentFilterPlugin *onetrustConsentFilterPlugin = nil;
RCT_EXPORT_METHOD(startConsentFilterPlugin:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    onetrustConsentFilterPlugin = [[RudderOnetrustConsentFilterPlugin alloc] initWithReactContext:resolve rejecter:reject];
    [onetrustConsentFilterPlugin setupOneTrust];
}

RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if (onetrustConsentFilterPlugin != nil) {
        [onetrustConsentFilterPlugin setConsentFilter];
    }
    resolve(nil);
}

@end
