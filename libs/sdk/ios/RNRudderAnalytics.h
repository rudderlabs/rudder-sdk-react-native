//
//  RNRudderAnalytics.h
//  CocoaAsyncSocket
//
//  Created by Dhawal on 28/07/20.
//

#import <Foundation/Foundation.h>
#import "RSIntegrationFactory.h"
#import "RSConfig.h"
#import "RSConsentFilter.h"

NS_ASSUME_NONNULL_BEGIN


@interface RNRudderAnalytics : NSObject

+ (void) addIntegration:(id<RSIntegrationFactory>) integration;
+ (RSConfig*) buildWithIntegrations:(RSConfigBuilder*)builder;
+ (void) setDBEncryption:(RSDBEncryption *)dbEncryption;
+ (RSDBEncryption *_Nullable) getDBEncryption;
+ (void) setConsentFilterPlugin:(id <RSConsentFilter> _Nonnull)consentFilter;
+ (id <RSConsentFilter> _Nullable) getConsentFilterPlugin;

@end

NS_ASSUME_NONNULL_END
