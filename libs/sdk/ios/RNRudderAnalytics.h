//
//  RNRudderAnalytics.h
//  CocoaAsyncSocket
//
//  Created by Dhawal on 28/07/20.
//

#import <Foundation/Foundation.h>
#import "RSIntegrationFactory.h"
#import "RSConfig.h"

NS_ASSUME_NONNULL_BEGIN


@interface RNRudderAnalytics : NSObject

+ (void) addIntegration:(id<RSIntegrationFactory>) integration;
+ (RSConfig*) buildWithIntegrations:(RSConfigBuilder*)builder;

@end

NS_ASSUME_NONNULL_END
