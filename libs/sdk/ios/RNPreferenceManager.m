//
//  RNPreferenceManager.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 20/07/23.
//

#import <Foundation/Foundation.h>

#import "RNPreferenceManager.h"
#import "RSLogger.h"
#if TARGET_OS_WATCH
#import <WatchKit/WKInterfaceDevice.h>
#endif

static RNPreferenceManager *instance;

@implementation RNPreferenceManager

NSString *const NativeApplicationBuildKey = @"rl_application_build_key";
NSString *const NativeApplicationVersionKey = @"rl_application_version_key";

NSString *const RNPrefsKey = @"rn_prefs";
NSString *const RNApplicationBuildKey = @"rn_application_build_key";
NSString *const RNApplicationVersionKey = @"rn_application_version_key";
NSString *const RNLastEventTimeStamp = @"rn_last_event_time_stamp";
NSString *const RNSessionAutoTrackStatus = @"rn_session_auto_track_status";
NSString *const RNSessionManualTrackStatus = @"rn_session_manual_track_status";

+ (instancetype)getInstance {
    if (instance == nil) {
        instance = [[RNPreferenceManager alloc] init];
    }
    return instance;
}

// App version

- (NSString* __nullable) getBuildNumber {
    return [[NSUserDefaults standardUserDefaults] valueForKey:RNApplicationBuildKey];
}

// saving the build number  to the NSUserDefaults
- (void) saveBuildNumber: (NSString* __nonnull) buildNumber {
    [[NSUserDefaults standardUserDefaults] setValue:buildNumber forKey:RNApplicationBuildKey];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

// saving the version number to the NSUserDefaults
- (NSString* __nullable) getVersionNumber {
    return [[NSUserDefaults standardUserDefaults] valueForKey:RNApplicationVersionKey];
}

- (void) saveVersionNumber: (NSString* __nonnull) versionNumber {
    [[NSUserDefaults standardUserDefaults] setValue:versionNumber forKey:RNApplicationVersionKey];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

// Session Tracking methods

- (void) saveLastEventTimeStamp:(NSNumber *) lastEventTimeStamp {
    [[NSUserDefaults standardUserDefaults] setValue:lastEventTimeStamp forKey:RNLastEventTimeStamp];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

- (NSNumber * __nullable) getLastEventTimeStamp {
    NSNumber *lastEventTimeStamp = [[NSUserDefaults standardUserDefaults] valueForKey:RNLastEventTimeStamp];
    if(lastEventTimeStamp == nil) {
        return nil;
    }
    return lastEventTimeStamp;
}

- (void) saveAutomaticSessionTrackingStatus: (BOOL) automaticTrackingStatus {
    [[NSUserDefaults standardUserDefaults] setBool:automaticTrackingStatus forKey:RNSessionAutoTrackStatus];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

- (BOOL) getAutomaticSessionTrackingStatus {
    return [[NSUserDefaults standardUserDefaults] boolForKey:RNSessionAutoTrackStatus];
}

- (void) saveManualSessionTrackingStatus: (BOOL) manualTrackingStatus {
    [[NSUserDefaults standardUserDefaults] setBool:manualTrackingStatus forKey:RNSessionManualTrackStatus];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

- (BOOL) getManualSessionTrackingStatus {
    return [[NSUserDefaults standardUserDefaults] boolForKey:RNSessionManualTrackStatus];
}

- (void) migrateAppInfoPreferencesWhenRNPrefDoesNotExist {
    if ([[NSUserDefaults standardUserDefaults] objectForKey:RNApplicationBuildKey] == nil &&
        [[NSUserDefaults standardUserDefaults] objectForKey:NativeApplicationBuildKey] != nil) {
        [self saveBuildNumber:[[NSUserDefaults standardUserDefaults] valueForKey:NativeApplicationBuildKey]];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
    if ([[NSUserDefaults standardUserDefaults] objectForKey:RNApplicationVersionKey] == nil &&
        [[NSUserDefaults standardUserDefaults] objectForKey:NativeApplicationVersionKey] != nil) {
        [self saveVersionNumber:[[NSUserDefaults standardUserDefaults] valueForKey:NativeApplicationVersionKey]];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
}

@end
