//
//  RNPreferenceManager.h
//  Pods
//
//  Created by Abhishek Pandey on 20/07/23.
//

#ifndef RNPreferenceManager_h
#define RNPreferenceManager_h

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNPreferenceManager : NSObject

extern NSString *const RNPrefsKey;
extern NSString *const RNLastEventTimeStamp;
extern NSString *const RNSessionAutoTrackStatus;
extern NSString *const RNSessionManualTrackStatus;

+ (instancetype) getInstance;

- (NSString* __nullable) getBuildNumber;
- (void) saveBuildNumber: (NSString* __nonnull) buildNumber;

- (NSString* __nullable) getVersionNumber;
- (void) saveVersionNumber: (NSString* __nonnull) versionNumber;

- (void) saveLastEventTimeStamp: (NSNumber *) lastEventTimeStamp;
- (NSNumber * __nullable) getLastEventTimeStamp;

- (void) saveAutomaticSessionTrackingStatus:(BOOL) automaticTrackingStatus;
- (BOOL) getAutomaticSessionTrackingStatus;

- (void) saveManualSessionTrackingStatus:(BOOL) manualTrackingStatus;
- (BOOL) getManualSessionTrackingStatus;

- (void) migrateAppInfoPreferencesWhenRNPrefDoesNotExist;

@end

NS_ASSUME_NONNULL_END



#endif /* RNPreferenceManager_h */
