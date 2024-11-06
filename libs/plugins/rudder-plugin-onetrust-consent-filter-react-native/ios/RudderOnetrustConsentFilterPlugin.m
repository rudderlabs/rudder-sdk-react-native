//
//  RudderOnetrustConsentFilterPlugin.m
//  rudder-plugin-onetrust-consent-filter-react-native
//
//  Created by Abhishek Pandey on 06/09/24.
//

#import "RudderOnetrustConsentFilterPlugin.h"
#import <Foundation/Foundation.h>
@import RudderOneTrustConsentFilter;
#import <RNRudderSdk/RNRudderAnalytics.h>


@implementation RudderOnetrustConsentFilterPlugin

- (instancetype)initWithReactContext:(RCTPromiseResolveBlock)resolve
                            rejecter:(RCTPromiseRejectBlock)reject {
    self = [super init];
    if (self) {
        self->resolve = resolve;
        self->reject = reject;
    }
    return self;
}

- (void)setupOneTrust:(NSString *)cdn withDomainIdentifier:(NSString *)domainIdentifier withLanguageCode:(NSString *)languageCode {
    // Implement the setup logic for OneTrust here
  [[OTPublishersHeadlessSDK shared] startSDKWithStorageLocation:cdn domainIdentifier:domainIdentifier languageCode:languageCode params:nil loadOffline:NO completionHandler:^(OTResponse *response) {
        if (response.status) {
          self->resolve(@YES);
        } else {
          self->resolve(@NO);
        }
    }];
    
    [[OTPublishersHeadlessSDK shared] addEventListener:self];
}

- (void)setConsentFilter {
    [RNRudderAnalytics setConsentFilterPlugin:[[RudderOneTrustConsentFilter alloc] init]];
}

@end
