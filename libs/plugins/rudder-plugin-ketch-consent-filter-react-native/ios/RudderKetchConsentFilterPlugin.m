//
//  RudderKetchConsentFilterPlugin.m
//  rudder-plugin-ketch-consent-filter-react-native
//
//  Created by Abhishek Pandey on 06/09/24.
//

#import "RudderKetchConsentFilterPlugin.h"
#import <Foundation/Foundation.h>
#import <RNRudderSdk/RNRudderAnalytics.h>


@implementation RudderKetchConsentFilterPlugin

- (instancetype)initWithReactContext:(RCTPromiseResolveBlock)resolve
                            rejecter:(RCTPromiseRejectBlock)reject {
    self = [super init];
    if (self) {
        self->resolve = resolve;
        self->reject = reject;
    }
    return self;
}

- (void)setupKetch {
    // Implement the setup logic for Ketch here
    
    // Assuming that consent is granted
    resolve(@YES);
    
    // Assuming that consent is rejected
//    resolve(@NO);
}

- (void)setConsentFilter {
    // Setup ketch integration
//    [RNRudderAnalytics setConsentFilterPlugin:[[RudderKetchConsentFilter alloc] init]];
}

@end
