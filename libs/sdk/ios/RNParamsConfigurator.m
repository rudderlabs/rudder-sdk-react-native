//
//  RNParamsConfigurator.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 02/08/23.
//

#import <Foundation/Foundation.h>
#import "RNParamsConfigurator.h"

@implementation RNParamsConfigurator

- (instancetype) initWithConfig:(NSDictionary*)config {
    self = [super init];
    if (self) {
        self->config = config;
        
        // Default values
        self.trackLifeCycleEvents = true;
        self.recordScreenViews = false;
        self.sessionTimeout = 300000L;
        self.autoSessionTracking = true;
        self.enableBackgroundMode = false;
    }
    return self;
}

-(RSConfigBuilder*)handleConfig {
    [self setConfigValues];
    [self setWriteKey];
    RSConfigBuilder *configBuilder = [self buildConfig];
    [self disableAutoConfigFlagsForNativeSDK:configBuilder];
    return configBuilder;
}

-(void)setConfigValues {
    if ([config objectForKey:@"trackAppLifecycleEvents"]) {
        self.trackLifeCycleEvents =  [config[@"trackAppLifecycleEvents"] boolValue];
    }
    if ([config objectForKey:@"recordScreenViews"]) {
        self.recordScreenViews = [config[@"recordScreenViews"] boolValue];
    }
    if ([config objectForKey:@"sessionTimeout"]) {
        self.sessionTimeout = [config[@"sessionTimeout"] intValue];
    }
    if ([config objectForKey:@"autoSessionTracking"]) {
        self.autoSessionTracking = [config[@"autoSessionTracking"] boolValue];
    }
    if ([config objectForKey:@"enableBackgroundMode"]) {
        self.enableBackgroundMode = [config[@"enableBackgroundMode"] boolValue];
    }
}

-(void)setWriteKey {
    self.writeKey = config[@"writeKey"];
}

-(RSConfigBuilder*)buildConfig {
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
    if ([config objectForKey:@"logLevel"]) {
        [configBuilder withLoglevel:[config[@"logLevel"] intValue]];
    }
    if ([config objectForKey:@"dbEncryption"]) {
        NSDictionary *dbEncryption = config[@"dbEncryption"];
        NSString *key = dbEncryption[@"key"];
        BOOL enable = [dbEncryption[@"enable"] boolValue];
        if (key != nil && [key length] > 0) {
            [configBuilder withDBEncryption:[[RSDBEncryption alloc] initWithKey:key enable:enable]];
        }
    }
    return configBuilder;
}

-(void) disableAutoConfigFlagsForNativeSDK:(RSConfigBuilder*)configBuilder {
    [configBuilder withTrackLifecycleEvens:false];
    [configBuilder withRecordScreenViews:false];
    [configBuilder withAutoSessionTracking:false];
    [configBuilder withEnableBackgroundMode:false];
}

@end

