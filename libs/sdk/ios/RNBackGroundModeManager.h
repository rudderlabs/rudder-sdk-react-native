//
//  RNBackGroundModeManager.h
//  Pods
//
//  Created by Abhishek Pandey on 20/07/23.
//

#ifndef RNBackGroundModeManager_h
#define RNBackGroundModeManager_h

#import <Foundation/Foundation.h>
#import "RSLogger.h"

@interface RNBackGroundModeManager : NSObject {
    BOOL enableBackgroundMode;
    BOOL isSemaphoreReleased;
#if !TARGET_OS_WATCH
    UIBackgroundTaskIdentifier backgroundTask;
#else
    dispatch_semaphore_t semaphore;
#endif
    
}

- (instancetype)initWithEnableBackgroundMode:(BOOL) enableBackgroundMode;
- (void) registerForBackGroundMode;

@end

#endif /* RNBackGroundModeManager_h */
