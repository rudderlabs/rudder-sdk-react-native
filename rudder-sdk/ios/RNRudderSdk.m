//
#import "RNRudderSdk.h"
#import "RudderClient.h"

@implementation RNRudderSdk

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(_initiateInstance: (NSString*) _writeKey
                        endPointUrl:(NSString*) _endPointUrl
                     flushQueueSize:(int)_flushQueueSize
                   dbCountThreshold:(int)_dbCountThreshold
                       sleepTimeOut:(int)_sleepTimeOut)
{
    [RudderClient _initiateInstance:_writeKey
                        endPointUrl:_endPointUrl
                     flushQueueSize:_flushQueueSize
                   dbCountThreshold:_dbCountThreshold
                       sleepTimeout:_sleepTimeOut];
}

RCT_EXPORT_METHOD(_logEvent: (NSString*) _eventType
                  eventName:(NSString*) _eventName
                     userId:(NSString*) _userId
             eventPropsJson:(NSString*)_eventPropsJson
              userPropsJson:(NSString*)_userPropsJson
           integrationsJson:(NSString*) _integrationsJson)
{
    [RudderClient _logEvent:_eventType
                  eventName:_eventName
                     userId:_userId
        eventPropertiesJson:_eventPropsJson
         userPropertiesJson:_userPropsJson
           integrationsJson:_integrationsJson];
}

@end
  
