#import "RNRudderSdkModule.h"
#import "RNRudderAnalytics.h"
#import "RSClient.h"
#import "RSConfig.h"
#import "RSLogger.h"
#import "RSOption.h"
#import <React/RCTBridge.h>

static RSClient *rsClient = nil;

@implementation RNRudderSdkModule

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(setup:(NSDictionary*)config options:(NSDictionary*) _options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if (rsClient == nil) {
        
        NSString* _writeKey = config[@"writeKey"];
        RSConfigBuilder* configBuilder = [[RSConfigBuilder alloc] init];
        
        if ([config objectForKey:@"dataPlaneUrl"]) {
            [configBuilder withDataPlaneUrl:config[@"dataPlaneUrl"]];
        }
        if ([config objectForKey:@"controlPlaneUrl"]) {
            [configBuilder withControlPlaneUrl:config[@"controlPlaneUrl"]];
        }
        if ([config objectForKey:@"flushQueueSize"]) {
            [configBuilder withFlushQueueSize:[config[@"flushQueueSize"] intValue]];
        }
        if ([config objectForKey:@"dbCountThreshold"]) {
            [configBuilder withDBCountThreshold:[config[@"dbCountThreshold"] intValue]];
        }
        if ([config objectForKey:@"sleepTimeOut"]) {
            [configBuilder withSleepTimeOut:[config[@"sleepTimeOut"] intValue]];
        }
        if ([config objectForKey:@"configRefreshInterval"]) {
            [configBuilder withConfigRefreshInteval:[config[@"configRefreshInterval"] intValue]];
        }
        if ([config objectForKey:@"trackAppLifecycleEvents"]) {
            [configBuilder withTrackLifecycleEvens:[config[@"trackAppLifecycleEvents"] boolValue]];
        }
        if ([config objectForKey:@"recordScreenViews"]) {
            [configBuilder withRecordScreenViews:[config[@"recordScreenViews"] boolValue]];
        }
        if ([config objectForKey:@"logLevel"]) {
            [configBuilder withLoglevel:[config[@"logLevel"] intValue]];
        }
        
        rsClient = [RSClient getInstance:_writeKey config:[RNRudderAnalytics buildWithIntegrations:configBuilder] options:[self getRudderOptionsObject:_options]];
        
        if ([config objectForKey:@"trackAppLifecycleEvents"]) {
            SEL selector = @selector(trackLifecycleEvents:);
            
            if ([rsClient respondsToSelector:selector]) {
                [rsClient performSelector:selector withObject:_bridge.launchOptions];
            }
        }
    }
    else{
        [RSLogger logDebug:@"Rudder Client already initialized, Ignoring the new setup call"];
    }
    resolve(nil);
}

RCT_EXPORT_METHOD(track:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Track call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setRSOption:[self getRudderOptionsObject:_options]];
    
    [[RSClient sharedInstance] trackWithBuilder:builder];
}
RCT_EXPORT_METHOD(screen:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Screen call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    // RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    // [builder setEventName:_event];
    // [builder setPropertyDict:_properties];
    // [builder setRSOption:[[RSOption alloc] init]];
    
    [[RSClient sharedInstance] screen:_event properties:_properties options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(identify:(NSString*)_userId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Identify call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    if([_userId isEqual:@""])
    {
        [[RSClient sharedInstance] identify:nil traits:_traits options:[self getRudderOptionsObject:_options]];
        return;
    }
    [[RSClient sharedInstance] identify:_userId traits:_traits options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(alias:(NSString*)_newId options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Alias call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    if([_newId isEqual:@""])
    {
        [RSLogger logWarn:@"Dropping the Alias call as newId can not be empty"];
        return;
    }
    [[RSClient sharedInstance] alias:_newId options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(group:(NSString*)_groupId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Group call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    if([_groupId isEqual:@""])
    {
        [RSLogger logWarn:@"Dropping the Group call as groupId can not be empty"];        
        return;
    }
    [[RSClient sharedInstance] group:_groupId traits:_traits options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(putDeviceToken:(NSString*)token)
{
    if ( token!=nil && [token length] != 0) {
        [RSClient putDeviceToken:token];
    }
}

RCT_EXPORT_METHOD(reset)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Reset call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    [[RSClient sharedInstance] reset];
}

RCT_EXPORT_METHOD(flush)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the Flush call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    [[RSClient sharedInstance] flush];
}

RCT_EXPORT_METHOD(optOut:(BOOL)optOut)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the optOut call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    [[RSClient sharedInstance] optOut:optOut];
}

RCT_EXPORT_METHOD(putAdvertisingId:(NSString*)id) {
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the setAdvertisingId call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    RSContext* rudderContext = [[RSClient sharedInstance] getContext];
    if (rudderContext != nil && id != nil && [id length] != 0) {
        [rudderContext putAdvertisementId:id];
    }
}

RCT_EXPORT_METHOD(putAnonymousId:(NSString*)id) {
    if (id != nil && [id length] != 0) {
    [RSClient putAnonymousId:id];
    }
}

RCT_EXPORT_METHOD(registerCallback:(NSString *)name callback: (RCTResponseSenderBlock)callback)
{
    // we will trigger the callback directly because ios native sdk's deal with static references
    callback(@[]);
}
// Migrated from Callbacks to Promise to support ES2016's async/await syntax on the RN Side
RCT_EXPORT_METHOD(getRudderContext:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if ([RSClient sharedInstance] == nil)
    {
        [RSLogger logWarn:@"Dropping the getRudderContext call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        resolve(nil);
        return;
    }
    NSDictionary* context = [[[RSClient sharedInstance] getContext] dict];
    resolve(context);
}

RCT_EXPORT_METHOD(getSessionId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    @try {
        if ([RSClient sharedInstance] == nil) {
            [RSLogger logWarn:@"Dropping the getSessionId call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
            return;
        }
        resolve([[RSClient sharedInstance] getSessionId]);
    }
    @catch (NSException *exception) {
        reject(exception.name, exception.reason, nil);
    }
    
}

RCT_EXPORT_METHOD(startSession) {
    if ([RSClient sharedInstance] == nil) {
        [RSLogger logWarn:@"Dropping the startSession call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    [[RSClient sharedInstance] startSession];
}

RCT_EXPORT_METHOD(endSession) {
    if ([RSClient sharedInstance] == nil) {
        [RSLogger logWarn:@"Dropping the endSession call as RudderClient is not initialized yet, Please use `await` keyword with the setup call"];
        return;
    }
    [[RSClient sharedInstance] endSession];
}

-(RSOption*) getRudderOptionsObject:(NSDictionary *) optionsDict {
    RSOption * options = [[RSOption alloc]init];
    NSArray *externalIdKeys = @[@"externalId", @"externalIds"];
    for (NSString *externalIdKey in externalIdKeys) {
        if([optionsDict objectForKey:externalIdKey])
        {
            NSArray *externalIdsArray =  [optionsDict objectForKey:externalIdKey];
            for(NSDictionary *externalId in externalIdsArray) {
                [options putExternalId:[externalId objectForKey:@"type"] withId:[externalId objectForKey:@"id"]];
            }
            break;
        }
    }
    if([optionsDict objectForKey:@"integrations"])
    {
        NSDictionary *integrationsDict = [optionsDict objectForKey:@"integrations"];
        for(NSString* key in integrationsDict)
        {
            [options putIntegration:key isEnabled:[[integrationsDict objectForKey:key] boolValue]];
        }
    }
    return options;
}

@end
