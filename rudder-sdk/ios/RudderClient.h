//
//  RudderClient.h
//  RudderSDKUnity
//
//  Created by Arnab Pal on 22/10/19.
//  Copyright © 2019 Rudderlabs. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RudderClient : NSObject

+ (void) _initiateInstance: (NSString*) writeKey
                       endPointUrl: (NSString*) endPointUrl
                    flushQueueSize: (int) flushQueueSize
                  dbCountThreshold: (int) dbCountThreshold
                      sleepTimeout: (int) sleepTimeout;

+ (void) _logEvent : (NSString*) eventType
          eventName: (NSString*) eventName
             userId: (NSString*) userId
eventPropertiesJson: (NSString*) eventPropertiesJson
 userPropertiesJson: (NSString*) userPropertiesJson
   integrationsJson: (NSString*) integrationsJson;

@end

NS_ASSUME_NONNULL_END
