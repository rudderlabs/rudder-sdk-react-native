#import "RNRudderSdkModule.h"
#import "RNRudderAnalytics.h"
#import "RSClient.h"
#import "RSConfig.h"
#import "RSLogger.h"
#import <React/RCTBridge.h>

@implementation RNRudderSdkModule

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(setup:(NSDictionary*)config resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
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

    RSClient* rsClient = [RSClient getInstance:_writeKey config:[RNRudderAnalytics buildWithIntegrations:configBuilder]];
    
    if ([config objectForKey:@"trackAppLifecycleEvents"]) {
        SEL selector = @selector(trackLifecycleEvents:);
                
        if ([rsClient respondsToSelector:selector]) {
            [rsClient performSelector:selector withObject:_bridge.launchOptions];
        }
    }

    resolve(nil);
}

RCT_EXPORT_METHOD(track:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil) return;
    RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setRSOption:[[RSOption alloc] init]];

    [[RSClient sharedInstance] trackWithBuilder:builder];
}
RCT_EXPORT_METHOD(screen:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil) return;
    RSMessageBuilder* builder = [[RSMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setRSOption:[[RSOption alloc] init]];

    [[RSClient sharedInstance] screenWithBuilder:builder];
}

RCT_EXPORT_METHOD(identify:(NSString*)_userId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if ([RSClient sharedInstance] == nil) return;
    [[RSClient sharedInstance] identify:_userId traits:_traits options:[[RSOption alloc] init]];
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

RCT_EXPORT_METHOD(checkIntegrationReady:(NSString*)integrationName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    // we will return true directly because ios native sdk's deal with static references
    resolve(@YES);
}

@end
