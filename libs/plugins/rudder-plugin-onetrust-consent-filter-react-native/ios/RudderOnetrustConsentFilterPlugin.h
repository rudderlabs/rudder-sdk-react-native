//
//  RudderOnetrustConsentFilterPlugin.h
//  Pods
//
//  Created by Abhishek Pandey on 06/09/24.
//

#ifndef RudderOnetrustConsentFilterPlugin_h
#define RudderOnetrustConsentFilterPlugin_h
#import <React/RCTBridgeModule.h>

@import OTPublishersHeadlessSDK;

@interface RudderOnetrustConsentFilterPlugin : NSObject<OTEventListener> {
    RCTPromiseResolveBlock resolve;
    RCTPromiseRejectBlock reject;
}

- (instancetype)initWithReactContext:(RCTPromiseResolveBlock)resolve
                            rejecter:(RCTPromiseRejectBlock)reject;

- (void)setupOneTrust;
- (void)setConsentFilter;

@end


#endif /* RudderOnetrustConsentFilterPlugin_h */
