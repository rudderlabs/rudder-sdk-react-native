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
    if ([RSClient sharedInstance] == nil) return;
    RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setRSOption:[self getRudderOptionsObject:_options]];
    
    [[RSClient sharedInstance] trackWithBuilder:builder];
}
RCT_EXPORT_METHOD(screen:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil) return;
    // RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    // [builder setEventName:_event];
    // [builder setPropertyDict:_properties];
    // [builder setRSOption:[[RSOption alloc] init]];
    
    [[RSClient sharedInstance] screen:_event properties:_properties options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(identify:(NSString*)_userId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil) return;
    if([_userId isEqual:@""])
    {
        [[RSClient sharedInstance] identify:nil traits:_traits options:[self getRudderOptionsObject:_options]];
        return;
    }
    [[RSClient sharedInstance] identify:_userId traits:_traits options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(putDeviceToken:(NSString*)token)
{
    if ([RSClient sharedInstance] == nil) return;
    RSContext* rudderContext = [[RSClient sharedInstance] getContext];
    if (rudderContext != nil && [token length] != 0) {
        [rudderContext putDeviceToken:token];
    }
}

RCT_EXPORT_METHOD(reset)
{
    if ([RSClient sharedInstance] == nil) return;
    [[RSClient sharedInstance] reset];
}

RCT_EXPORT_METHOD(optOut:(BOOL)optOut)
{
    if ([RSClient sharedInstance] == nil) return;
    [[RSClient sharedInstance] optOut:optOut];
}

RCT_EXPORT_METHOD(setAdvertisingId:(NSString*)id) {
    if ([RSClient sharedInstance] == nil) return;
    RSContext* rudderContext = [[RSClient sharedInstance] getContext];
    if (rudderContext != nil) {
        [rudderContext putAdvertisementId:id];
    }
}

RCT_EXPORT_METHOD(setAnonymousId:(NSString*)id) {
    [RSClient setAnonymousId:id];
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
        resolve(nil);
        return;
    }
    NSDictionary* context = [[[RSClient sharedInstance] getContext] dict];
    resolve(context);
}

-(RSOption*) getRudderOptionsObject:(NSDictionary *) optionsDict {
    RSOption * options = [[RSOption alloc]init];
    if([optionsDict objectForKey:@"externalIds"])
    {
        NSArray *externalIdsArray =  [optionsDict objectForKey:@"externalIds"];
        for(NSDictionary *externalId in externalIdsArray) {
            [options putExternalId:[externalId objectForKey:@"type"] withId:[externalId objectForKey:@"id"]];
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
