//
#import "RNRudderSdkModule.h"
#import "RudderClient.h"
#import "RudderConfig.h"
#import "RudderLogger.h"

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
    
    RudderConfigBuilder* configBuilder = [[RudderConfigBuilder alloc] init];
    
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
        [configBuilder withConfigRefreshInterval:[config[@"configRefreshInterval"] intValue]];
    }
    if ([config objectForKey:@"trackAppLifecycleEvents"]) {
        [configBuilder withTrackAppLifecycleEvents:[config[@"trackAppLifecycleEvents"] boolValue]];
    }
    if ([config objectForKey:@"recordScreenViews"]) {
        [configBuilder withRecordScreenViews:[config[@"recordScreenViews"] boolValue]];
    }
    if ([config objectForKey:@"logLevel"]) {
        [configBuilder withLoglevel:[config[@"logLevel"] intValue]];
    }
    
    [RudderClient getInstance:_writeKey config:[configBuilder build]];
    
    resolve(nil);
}

RCT_EXPORT_METHOD(track:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RudderClient sharedInstance] == nil) return;
    RudderMessageBuilder* builder = [[RudderMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setUserProperty:_userProperties];
    [builder setRudderOption:[[RudderOption alloc] initWithDict:_options]];
    
    [[RudderClient sharedInstance] trackWithBuilder:builder];
}
RCT_EXPORT_METHOD(screen:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options)
{
    if ([RudderClient sharedInstance] == nil) return;
    RudderMessageBuilder* builder = [[RudderMessageBuilder alloc] init];
    [builder setEventName:_event];
    [builder setPropertyDict:_properties];
    [builder setUserProperty:_userProperties];
    [builder setRudderOption:[[RudderOption alloc] initWithDict:_options]];
    
    [[RudderClient sharedInstance] screenWithBuilder:builder];
}

RCT_EXPORT_METHOD(identify:(NSString*)_userId traits:(NSDictionary*)_traits options:(NSDictionary*)_options)
{
    if ([RudderClient sharedInstance] == nil) return;
    [[RudderClient sharedInstance] identify:_userId traits:_traits options:_options];
}

RCT_EXPORT_METHOD(reset)
{
    if ([RudderClient sharedInstance] == nil) return;
    [[RudderClient sharedInstance] reset];
}

@end
