//
//  RudderClient.m
//  RudderSDKUnity
//
//  Created by Arnab Pal on 22/10/19.
//  Copyright © 2019 Rudderlabs. All rights reserved.
//

#import "RudderClient.h"
#import "EventRepository.h"
#import "RudderConfig.h"
#import "RudderMessage.h"
#import "RudderMessageBuilder.h"

static RudderClient *_instance = nil;
static EventRepository *_repository = nil;

@implementation RudderClient

+ (void)_initiateInstance:(NSString *)writeKey endPointUrl:(NSString *)endPointUrl flushQueueSize:(int)flushQueueSize dbCountThreshold:(int)dbCountThreshold sleepTimeout:(int)sleepTimeout {
    if (_instance == nil) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            _instance = [[self alloc] init];
            
            RudderConfig *config = [[RudderConfig alloc] init];
            config.endPointUrl = endPointUrl;
            config.flushQueueSize = flushQueueSize;
            config.dbCountThreshold = dbCountThreshold;
            config.sleepTimeout = sleepTimeout;
            
            _repository = [EventRepository initiate:writeKey config:config];
        });
    }
}

+ (void) _logEvent:(NSString *)eventType eventName:(NSString *)eventName userId:(NSString *)userId eventPropertiesJson:(NSString *)eventPropertiesJson userPropertiesJson:(NSString *)userPropertiesJson integrationsJson:(NSString *)integrationsJson {
    NSError *error;
    
    RudderMessageBuilder *builder = [[RudderMessageBuilder alloc] init];
    [builder setEventName:eventName];
    
    NSDictionary *eventProperties = [NSJSONSerialization JSONObjectWithData:[eventPropertiesJson dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:&error];
    [builder setPropertyDict:eventProperties];
    
    NSDictionary *userProperties = [NSJSONSerialization JSONObjectWithData:[userPropertiesJson dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:&error];;
    [builder setUserProperty:userProperties];
    
    [builder setUserId:userId];
    
    RudderMessage *message = [builder build];
    NSDictionary *integrations = [NSJSONSerialization JSONObjectWithData:[integrationsJson dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:&error];;
    message.integrations = integrations;
    
    message.type = eventType;
    
    if (_repository != nil) {
        [_repository dump:message];
    }
}

@end
