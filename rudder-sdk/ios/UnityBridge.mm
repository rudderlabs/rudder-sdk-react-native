//
//  UnityBridge.m
//  RudderSDKUnity
//
//  Created by Arnab Pal on 23/10/19.
//  Copyright © 2019 Rudderlabs. All rights reserved.
//

#import "UnityBridge.h"
#import <sqlite3.h>

void _initiateInstance(const char* _writeKey,
                       const char* _endPointUrl,
                       const int _flushQueueSize,
                       const int _dbCountThreshold,
                       const int _sleepTimeout) {
    [RudderClient _initiateInstance:[[NSString alloc] initWithUTF8String:_writeKey]
                        endPointUrl:[[NSString alloc] initWithUTF8String:_endPointUrl]
                     flushQueueSize:_flushQueueSize
                   dbCountThreshold:_dbCountThreshold
                       sleepTimeout:_sleepTimeout];
}

void _logEvent(const char* _eventType,
               const char* _eventName,
               const char* _userId,
               const char* _eventPropertyJson,
               const char* _userPropertyJson,
               const char* _integrationJson) {
    [RudderClient _logEvent:[[NSString alloc] initWithUTF8String:_eventType]
                  eventName:[[NSString alloc] initWithUTF8String:_eventName]
                     userId:[[NSString alloc] initWithUTF8String:_userId]
        eventPropertiesJson:[[NSString alloc] initWithUTF8String:_eventPropertyJson]
         userPropertiesJson:[[NSString alloc] initWithUTF8String:_userPropertyJson]
           integrationsJson:[[NSString alloc] initWithUTF8String:_integrationJson]];
}

void _serializeSqlite() {
    sqlite3_config(SQLITE_CONFIG_SERIALIZED);
}
