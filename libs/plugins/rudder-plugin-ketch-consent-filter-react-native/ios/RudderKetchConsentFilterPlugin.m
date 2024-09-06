//
//  RudderKetchConsentFilterPlugin.m
//  rudder-plugin-ketch-consent-filter-react-native
//
//  Created by Abhishek Pandey on 06/09/24.
//

#import "RudderKetchConsentFilterPlugin.h"
#import <Foundation/Foundation.h>
@import RudderOneTrustConsentFilter;
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

- (void)setupOneTrust {
    // Implement the setup logic for OneTrust here
    [[OTPublishersHeadlessSDK shared] startSDKWithStorageLocation:@"<STORAGE_LOCATION>" domainIdentifier:@"<DOMAIN_IDENTIFIER>" languageCode:@"en" params:nil loadOffline:NO completionHandler:^(OTResponse *response) {
        if (response.status) {
            
        }
    }];
    
    [[OTPublishersHeadlessSDK shared] addEventListener:self];
    
    // Assuming that consent is granted
    resolve(@YES);
    
    // Assuming that consent is rejected
//    resolve(@NO);
}

- (void)setConsentFilter {
    [RNRudderAnalytics setConsentFilterPlugin:[[RudderOneTrustConsentFilter alloc] init]];
}

@end
