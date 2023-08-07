//
//  RNApplicationLifeCycleManager.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 20/07/23.
//

#import <Foundation/Foundation.h>
#import "RNApplicationLifeCycleManager.h"
#import "RNUIViewController+RNScreen.h"
#import "RNWKInterfaceController+RNScreen.h"

@implementation RNApplicationLifeCycleManager


- (instancetype)initWithTrackLifecycleEvents:(BOOL)trackLifecycleEvents andBackGroundModeManager:(RNBackGroundModeManager *)backGroundModeManager withLaunchOptions:(id)launchOptions withSessionPlugin:(RNUserSessionPlugin *)session {
    self = [super init];
    if(self){
        self->trackLifecycleEvents = trackLifecycleEvents;
        self->preferenceManager = [RNPreferenceManager getInstance];
        self->firstForeGround = YES;
        self->backGroundModeManager = backGroundModeManager;
        self->session = session;
        [self applicationDidFinishLaunchingWithOptions: launchOptions];
    }
    return self;
}

#if !TARGET_OS_WATCH
- (void)trackApplicationLifeCycle {
    [RSLogger logVerbose:@"RNApplicationLifeCycleManager: trackApplicationLifeCycle: Registering for Application Life Cycle Notifications"];
    NSNotificationCenter *nc = [NSNotificationCenter defaultCenter];
    for (NSString *name in @[ UIApplicationDidEnterBackgroundNotification,
                              UIApplicationWillEnterForegroundNotification ]) {
        [nc addObserver:self selector:@selector(handleAppStateNotification:) name:name object:UIApplication.sharedApplication];
    }
}

- (void)handleAppStateNotification: (NSNotification*) notification {
    if ([notification.name isEqualToString:UIApplicationWillEnterForegroundNotification]) {
        [self applicationWillEnterForeground];
    } else if ([notification.name isEqualToString: UIApplicationDidEnterBackgroundNotification]) {
        [self applicationDidEnterBackground];
    }
}

#else
- (void)trackApplicationLifeCycle {
    [RSLogger logVerbose:@"RNApplicationLifeCycleManager: trackApplicationLifeCycle: Registering for Application Life Cycle Notifications"];
    NSNotificationCenter *nc = [NSNotificationCenter defaultCenter];
    for (NSString *name in @[ WKApplicationWillResignActiveNotification,
                              WKApplicationDidBecomeActiveNotification ]) {
        [nc addObserver:self selector:@selector(handleAppStateNotification:) name:name object:nil];
    }
}

- (void)handleAppStateNotification: (NSNotification*) notification {
    if ([notification.name isEqualToString: WKApplicationDidBecomeActiveNotification]) {
        [self applicationWillEnterForeground];
    } else if ([notification.name isEqualToString: WKApplicationWillResignActiveNotification]) {
        [self applicationDidEnterBackground];
    }
}
#endif

- (void)applicationDidFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    NSString *previousVersion = [preferenceManager getVersionNumber];
    NSString *previousBuildNumber = [preferenceManager getBuildNumber];
    
    NSString *currentVersion = [[NSBundle mainBundle] infoDictionary][@"CFBundleShortVersionString"];
    NSString *currentBuildNumber = [[NSBundle mainBundle] infoDictionary][@"CFBundleVersion"];
    
    [self saveCurrentBuildNumberAndVersion:currentBuildNumber currentVersion:currentVersion];
    
    if (!self->trackLifecycleEvents) {
        return;
    }
    
    if (!previousVersion) {
        [self->session saveEventTimestamp];
        [RSLogger logVerbose:@"RNApplicationLifeCycleManager: applicationDidFinishLaunchingWithOptions: Tracking Application Installed"];
        [[RSClient sharedInstance] track:@"Application Installed" properties:@{
            @"version": currentVersion,
            @"build": currentBuildNumber
        }];
    } else if (![previousVersion isEqualToString:currentVersion]) {
        [self->session saveEventTimestamp];
        [RSLogger logVerbose:@"RNApplicationLifeCycleManager: applicationDidFinishLaunchingWithOptions: Tracking Application Updated"];
        [[RSClient sharedInstance] track:@"Application Updated" properties:@{
            @"previous_version" : previousVersion ?: @"",
            @"version": currentVersion,
            @"previous_build": previousBuildNumber ?: @"",
            @"build": currentBuildNumber
        }];
    }
    [self sendApplicationOpenedOnLaunch:launchOptions withVersion:currentVersion];
}

- (void)saveCurrentBuildNumberAndVersion:(NSString *)currentBuildNumber currentVersion:(NSString *)currentVersion {
    [preferenceManager saveVersionNumber:currentVersion];
    [preferenceManager saveBuildNumber:currentBuildNumber];
}

- (void) sendApplicationOpenedOnLaunch:(NSDictionary *)launchOptions withVersion:(NSString *) version {
    NSMutableDictionary *applicationOpenedProperties = [[NSMutableDictionary alloc] init];
    [applicationOpenedProperties setObject:@NO forKey:@"from_background"];
    if (version != nil) {
        [applicationOpenedProperties setObject:version forKey:@"version"];
    }
#if !TARGET_OS_WATCH
    NSString *referring_application = [[NSString alloc] initWithFormat:@"%@", launchOptions[UIApplicationLaunchOptionsSourceApplicationKey] ?: @""];
    if ([referring_application length]) {
        [applicationOpenedProperties setObject:referring_application forKey:@"referring_application"];
    }
    NSString *url = [[NSString alloc] initWithFormat:@"%@", launchOptions[UIApplicationLaunchOptionsURLKey] ?: @""];
    if ([url length]) {
        [applicationOpenedProperties setObject:url forKey:@"url"];
    }
#endif
    [self sendApplicationOpenedWithProperties:applicationOpenedProperties];
}

- (void)applicationWillEnterForeground {
#if TARGET_OS_WATCH
    if(self->firstForeGround) {
        self->firstForeGround = NO;
        return;
    }
#endif
    [self->backGroundModeManager registerForBackGroundMode];
    if (!self->trackLifecycleEvents) {
        return;
    }
    [self->session startNewSessionIfCurrentIsExpired];
    [self sendApplicationOpenedWithProperties:@{@"from_background" : @YES}];
}

- (void) sendApplicationOpenedWithProperties:(NSDictionary *) properties {
    [self->session saveEventTimestamp];
    [RSLogger logVerbose:@"RNApplicationLifeCycleManager: sendApplicationOpenedWithProperties: Tracking Application Opened"];
    [[RSClient sharedInstance] track:@"Application Opened" properties:properties];
}

- (void) applicationDidEnterBackground {
    if (!self->trackLifecycleEvents) {
        return;
    }
    [self->session saveEventTimestamp];
    [RSLogger logVerbose:@"RNApplicationLifeCycleManager: applicationDidEnterBackground: Tracking Application Backgrounded"];
    [[RSClient sharedInstance] track:@"Application Backgrounded"];
}

- (void) prepareScreenRecorder {
#if TARGET_OS_WATCH
    [WKInterfaceController initSessionPlugin:self->session];
    [WKInterfaceController rudder_rn_swizzleView];
#else
    [UIViewController initSessionPlugin:self->session];
    [UIViewController rudder_rn_swizzleView];
#endif
}

@end
