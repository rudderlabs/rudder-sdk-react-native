//
//  RNRudderAnalytics.h
//  CocoaAsyncSocket
//
//  Created by Dhawal on 28/07/20.
//

#import <Foundation/Foundation.h>

// Forward-declare Rudder pod types instead of importing their headers, so that
// consumers (including Swift via `import RNRudderSdk`) do not pick up a second
// copy of these symbols attributed to the RNRudderSdk module. The .m file
// imports the real headers where the full type info is needed.
@protocol RSIntegrationFactory;
@class RSConfig;
@class RSConfigBuilder;
@class RSDBEncryption;

NS_ASSUME_NONNULL_BEGIN


@interface RNRudderAnalytics : NSObject

+ (void) addIntegration:(id<RSIntegrationFactory>) integration;
+ (RSConfig*) buildWithIntegrations:(RSConfigBuilder*)builder;
+ (void) setDBEncryption:(RSDBEncryption *)dbEncryption;
+ (RSDBEncryption *_Nullable) getDBEncryption;

@end

NS_ASSUME_NONNULL_END
