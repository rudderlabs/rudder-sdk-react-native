//
//  RudderKetchConsentFilterPlugin.h
//  Pods
//
//  Created by Abhishek Pandey on 06/09/24.
//

#ifndef RudderKetchConsentFilterPlugin_h
#define RudderKetchConsentFilterPlugin_h
#import <React/RCTBridgeModule.h>


@interface RudderKetchConsentFilterPlugin : NSObject {
    RCTPromiseResolveBlock resolve;
    RCTPromiseRejectBlock reject;
}

- (instancetype)initWithReactContext:(RCTPromiseResolveBlock)resolve
                            rejecter:(RCTPromiseRejectBlock)reject;

- (void)setupKetch;
- (void)setConsentFilter;

@end


#endif /* RudderKetchConsentFilterPlugin_h */
