#import "RNRudderSdkModule.h"
#import "RNRudderAnalytics.h"
#import "RSClient.h"
#import "RSConfig.h"
#import "RSLogger.h"
#import "RSOption.h"
#import "RSMessageBuilder.h"
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
        self->configParams = [[RNParamsConfigurator alloc] initWithConfig:config];
        RSConfigBuilder *configBuilder = [self->configParams handleConfig];

        [RSLogger logDebug:@"setup: Initiating RNPreferenceManager"];
        self->preferenceManager = [RNPreferenceManager getInstance];

        rsClient = [RSClient getInstance:self->configParams.writeKey config:[RNRudderAnalytics buildWithIntegrations:configBuilder] options:[self getRudderOptionsObject:_options]];

        [RSLogger logDebug:@"setup: Initiating RNUserSessionPlugin"];
        self->session = [[RNUserSessionPlugin alloc] initWithAutomaticSessionTrackingStatus:self->configParams.autoSessionTracking withLifecycleEventsTrackingStatus:self->configParams.trackLifeCycleEvents withSessionTimeout:self->configParams.sessionTimeout];
        [self->session handleSessionTracking];

        [RSLogger logDebug:@"setup: Initiating RNBackGroundModeManager"];
        self->backGroundModeManager = [[RNBackGroundModeManager alloc] initWithEnableBackgroundMode:self->configParams.enableBackgroundMode];

        [RSLogger logDebug:@"setup: Initiating RNApplicationLifeCycleManager"];
        self->applicationLifeCycleManager = [[RNApplicationLifeCycleManager alloc] initWithTrackLifecycleEvents:self->configParams.trackLifeCycleEvents andBackGroundModeManager:self->backGroundModeManager withLaunchOptions:_bridge.launchOptions withSessionPlugin:self->session];
        [self->applicationLifeCycleManager trackApplicationLifeCycle];

        if (self->configParams.recordScreenViews) {
            [RSLogger logDebug:@"setup: Enabling automatic recording of screen views"];
            [self->applicationLifeCycleManager prepareScreenRecorder];
        }
        self->initialized = YES;
    }
    else{
        [RSLogger logDebug:@"Rudder Client already initialized, Ignoring the new setup call"];
    }
    resolve(nil);
}

-(BOOL) isRudderClientInitializedAndReady {
    if ([RSClient sharedInstance] == nil || self->initialized == NO) {
        [RSLogger logWarn:@"Dropping the call as RudderClient is not initialized yet. Please use `await` keyword with the setup call"];
        return NO;
    }
    return YES;
}

RCT_EXPORT_METHOD(track:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [self->session saveEventTimestamp];
    RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setRSOption:[self getRudderOptionsObject:_options]];

    [[RSClient sharedInstance] trackWithBuilder:builder];
}
RCT_EXPORT_METHOD(screen:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    // RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    // [builder setEventName:_event];
    // [builder setPropertyDict:_properties];
    // [builder setRSOption:[[RSOption alloc] init]];

    [self->session saveEventTimestamp];
    [[RSClient sharedInstance] screen:_event properties:_properties options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(identify:(NSString*)_userId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [self->session saveEventTimestamp];
    if([_userId isEqual:@""])
    {
        [[RSClient sharedInstance] identify:nil traits:_traits options:[self getRudderOptionsObject:_options]];
        return;
    }
    [[RSClient sharedInstance] identify:_userId traits:_traits options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(alias:(NSString*)_newId options:(NSDictionary*)_options)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    if([_newId isEqual:@""])
    {
        [RSLogger logWarn:@"Dropping the Alias call as newId can not be empty"];
        return;
    }
    [self->session saveEventTimestamp];
    [[RSClient sharedInstance] alias:_newId options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(group:(NSString*)_groupId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    if([_groupId isEqual:@""])
    {
        [RSLogger logWarn:@"Dropping the Group call as groupId can not be empty"];
        return;
    }
    [self->session saveEventTimestamp];
    [[RSClient sharedInstance] group:_groupId traits:_traits options:[self getRudderOptionsObject:_options]];
}

RCT_EXPORT_METHOD(putDeviceToken:(NSString*)token)
{
    if ( token!=nil && [token length] != 0) {
        [RSClient putDeviceToken:token];
    }
}

RCT_EXPORT_METHOD(reset:(BOOL) clearAnonymousId)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [[RSClient sharedInstance] reset:clearAnonymousId];
}

RCT_EXPORT_METHOD(flush)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [[RSClient sharedInstance] flush];
}

RCT_EXPORT_METHOD(optOut:(BOOL)optOut)
{
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [[RSClient sharedInstance] optOut:optOut];
}

RCT_EXPORT_METHOD(putAdvertisingId:(NSString*)id) {
    if (![self isRudderClientInitializedAndReady]) {
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
    if (![self isRudderClientInitializedAndReady]) {
        resolve(nil);
        return;
    }
    NSDictionary* context = [[[RSClient sharedInstance] getContext] dict];
    resolve(context);
}

RCT_EXPORT_METHOD(startSession:(NSString *)sessionId) {
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [self->session enableManualSessionParams];
    if ([sessionId length] == 0) {
        [self->session startSession];
        [RSLogger logVerbose:@"setup: startSession: starting manual session"];
        return;
    }
    [self->session startSession:[sessionId longLongValue]];
    [RSLogger logVerbose:[NSString stringWithFormat:@"setup: starting manual session with id: %lld", [sessionId longLongValue]]];
}

RCT_EXPORT_METHOD(endSession) {
    if (![self isRudderClientInitializedAndReady]) {
        return;
    }
    [self->session endSession];
    [RSLogger logVerbose:@"setup: ending session"];
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
