//
//  UnityBridge.h
//  RudderSDKUnity
//
//  Created by Arnab Pal on 23/10/19.
//  Copyright © 2019 Rudderlabs. All rights reserved.
//

#import "RudderSDKUnity.h"

#ifdef __cplusplus
extern "C" {
#endif
    void _initiateInstance(const char* _writeKey,
                           const char* _endPointUrl,
                           const int _flushQueueSize,
                           const int _dbCountThreshold,
                           const int _sleepTimeout);
    
    void _logEvent(const char* _eventType,
                   const char* _eventName,
                   const char* _userId,
                   const char* _eventPropertyJson,
                   const char* _userPropertyJson,
                   const char* _integrationJson);

#ifdef __cplusplus
}
#endif
