#import "RNRudderSdkModule.h"
#import "RNRudderAnalytics.h"
#import "RSClient.h"
#import "RSConfig.h"
#import "RSLogger.h"
#import "RSOption.h"
#import "RSMessageBuilder.h"

@implementation RNRudderSdkModule
RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeRudderBridgeSpecJSI>(params);
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

- (void)setup:(NSDictionary * _Nullable)configuration options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    self->configParams = [[RNParamsConfigurator alloc] initWithConfig:configuration];
    RSConfigBuilder *configBuilder = [self->configParams handleConfig];
    
    [RSLogger logDebug:@"setup: Initiating RNPreferenceManager"];
    self->preferenceManager = [RNPreferenceManager getInstance];
    [self->preferenceManager migrateAppInfoPreferencesWhenRNPrefDoesNotExist];
    
    [RSClient getInstance:self->configParams.writeKey config:[RNRudderAnalytics buildWithIntegrations:configBuilder] options:[self getRudderOptionsObject:options]];
    
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
  } else {
    [RSLogger logDebug:@"Rudder Client already initialized, Ignoring the new setup call"];
  }
  resolve(nil);
}

-(BOOL) isRudderClientInitializedAndReady {
  if (self->initialized == NO || [RSClient sharedInstance] == nil) {
    [RSLogger logWarn:@"Dropping the call as RudderClient is not initialized yet. Please use `await` keyword with the setup call"];
    return NO;
  }
  return YES;
}

- (void)track:(NSString *)event properties:(NSDictionary *)properties options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [self->session saveEventTimestamp];
  RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
  [builder setEventName:event];
  [builder setPropertyDict:properties];
  [builder setRSOption:[self getRudderOptionsObject:options]];
  
  [[RSClient sharedInstance] trackWithBuilder:builder];
  resolve(nil);
}

- (void)screen:(NSString *)name properties:(NSDictionary *)properties options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [self->session saveEventTimestamp];
  [[RSClient sharedInstance] screen:name properties:properties options:[self getRudderOptionsObject:options]];
  resolve(nil);
}

- (void)identify:(NSString *)userId traits:(NSDictionary *)traits options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [self->session saveEventTimestamp];
  if([userId isEqual:@""]) {
    [[RSClient sharedInstance] identify:nil traits:traits options:[self getRudderOptionsObject:options]];
  } else {
    [[RSClient sharedInstance] identify:userId traits:traits options:[self getRudderOptionsObject:options]];
  }
  resolve(nil);
}

- (void)alias:(NSString *)newId previousId:(NSString *)previousId options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  if([newId isEqual:@""]) {
    [RSLogger logWarn:@"Dropping the Alias call as newId can not be empty"];
    resolve(nil);
    return;
  }
  [self->session saveEventTimestamp];
  [[RSClient sharedInstance] alias:newId previousId:previousId options:[self getRudderOptionsObject:options]];
  resolve(nil);
}

- (void)group:(NSString *)groupId traits:(NSDictionary *)traits options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  if([groupId isEqual:@""]) {
    [RSLogger logWarn:@"Dropping the Group call as groupId can not be empty"];
    resolve(nil);
    return;
  }
  [self->session saveEventTimestamp];
  [[RSClient sharedInstance] group:groupId traits:traits options:[self getRudderOptionsObject:options]];
  resolve(nil);
}

- (void)putDeviceToken:(NSString *)token resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (token != nil && [token length] != 0) {
    [RSClient putDeviceToken:token];
  }
  resolve(nil);
}

- (void)reset:(BOOL)clearAnonymousId resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [[RSClient sharedInstance] reset:clearAnonymousId];
  resolve(nil);
}

- (void)flush:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [[RSClient sharedInstance] flush];
  resolve(nil);
}

- (void)optOut:(BOOL)optOut resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [[RSClient sharedInstance] optOut:optOut];
  resolve(nil);
}

- (void)putAdvertisingId:(NSString *)id resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  [RSClient putAdvertisingId:id];
  resolve(nil);
}

- (void)clearAdvertisingId:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [[RSClient sharedInstance] clearAdvertisingId];
  resolve(nil);
}

- (void)putAnonymousId:(NSString *)id resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (id != nil && [id length] != 0) {
    [RSClient putAnonymousId:id];
  }
  resolve(nil);
}

- (void)registerCallback:(nonnull NSString *)integrationName callback:(nonnull RCTResponseSenderBlock)callback {
  // we will trigger the callback directly because ios native sdk's deal with static references
  callback(@[]);
}

- (void)getRudderContext:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject
{
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  NSDictionary* context = [[[RSClient sharedInstance] getContext] dict];
  resolve(context);
}

- (void)startSession:(NSString *)sessionId resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [self->session enableManualSessionParams];
  if ([sessionId length] == 0) {
    [self->session startSession];
    [RSLogger logVerbose:@"setup: startSession: starting manual session"];
  } else {
    [self->session startSession:[sessionId longLongValue]];
    [RSLogger logVerbose:[NSString stringWithFormat:@"setup: starting manual session with id: %lld", [sessionId longLongValue]]];
  }
  resolve(nil);
}

- (void)endSession:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  [self->session endSession];
  [RSLogger logVerbose:@"Ending session"];
  resolve(nil);
}

- (void)getSessionId:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
  if (![self isRudderClientInitializedAndReady]) {
    resolve(nil);
    return;
  }
  NSNumber *sessionId = [self->session getSessionId];
  resolve(sessionId);
}

-(RSOption*) getRudderOptionsObject:(NSDictionary *) optionsDict {
  RSOption * options = [[RSOption alloc]init];
  @try {
    optionsDict = [self removeExternalIdsIfExternalIdExists:optionsDict];
    for (NSString *key in optionsDict) {
      if ([key isEqualToString:@"externalId"]) {
        [self setExternalId:key withOptionsDict:optionsDict andOptions:options];
      } else if ([key isEqualToString:@"externalIds"]) {
        [self setExternalId:key withOptionsDict:optionsDict andOptions:options];
      } else if ([key isEqualToString:@"integrations"]) {
        [self setIntegrations:optionsDict andOptions:options];
      } else {
        [self setCustomContext:key withOptionsDict:optionsDict options:options];
      }
    }
  } @catch (NSException *exception) {
    [RSLogger logWarn:[NSString stringWithFormat:@"Error occured while handling options object: %@", exception]];
  }
  return options;
}

// For legacy reason we are still supporting "externalIds". First priority is given to "externalId".
- (NSDictionary *)removeExternalIdsIfExternalIdExists:(NSDictionary *)optionsDict {
  if (optionsDict == nil) return nil;
  
  NSMutableDictionary *mutableOptionsDict = [optionsDict mutableCopy];
  if ([mutableOptionsDict objectForKey:@"externalId"] && [mutableOptionsDict objectForKey:@"externalIds"]) {
    [mutableOptionsDict removeObjectForKey:@"externalIds"];
  }
  
  return [mutableOptionsDict copy];
}

-(void) setExternalId:(NSString*) key withOptionsDict:(NSDictionary *) optionsDict andOptions:(RSOption *) options {
  if([optionsDict objectForKey:key]) {
    NSArray *externalIdsArray =  [optionsDict objectForKey:key];
    for(NSDictionary *externalId in externalIdsArray) {
      id type = [externalId objectForKey:@"type"];
      id idValue = [externalId objectForKey:@"id"];
      if (type != nil && idValue != nil) {
        [options putExternalId:type withId:idValue];
      }
    }
  }
}

-(void) setIntegrations:(NSDictionary *) optionsDict andOptions:(RSOption *) options {
  NSDictionary *integrationsDict = [optionsDict objectForKey:@"integrations"];
  for(NSString* key in integrationsDict) {
    id value = [integrationsDict objectForKey:key];
    if (value != nil && value != [NSNull null] && [value isKindOfClass:[NSNumber class]]) {
      [options putIntegration:key isEnabled:[[integrationsDict objectForKey:key] boolValue]];
    }
  }
}

-(void) setCustomContext:(NSString*) key withOptionsDict:(NSDictionary *) optionsDict options:(RSOption *) options {
  id value = optionsDict[key];
  if ([value isKindOfClass:[NSDictionary class]] && [(NSDictionary *)value count] > 0) {
    [options putCustomContext:value withKey:key];
  }
}

@end
