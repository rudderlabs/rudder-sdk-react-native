//
//  RNBackGroundModeManager.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 20/07/23.
//

#import <Foundation/Foundation.h>
#import "RNBackGroundModeManager.h"

@implementation RNBackGroundModeManager

- (instancetype)initWithEnableBackgroundMode:(BOOL) enableBackgroundMode {
    self = [super init];
    if(self){
        self->enableBackgroundMode = enableBackgroundMode;
        [RSLogger logDebug:@"RNBackGroundModeManager: Init: Initializing BackgroundMode Manager"];
#if !TARGET_OS_WATCH
        [self resetBackgroundTask];
#endif
        [self registerForBackGroundMode];
    }
    return self;
}

-(void) resetBackgroundTask {
    self->backgroundTask = UIBackgroundTaskInvalid;
}


- (void) registerForBackGroundMode {
    if(self->enableBackgroundMode) {
#if !TARGET_OS_WATCH
        [self registerBackGroundTask];
#else
        [self askForAssertionWithSemaphore];
#endif
    }
}

#if !TARGET_OS_WATCH
/*
 Methods useful for registering for Background Run Time after the app has been backgrounded for the platforms iOS, tvOS
 */
- (void) registerBackGroundTask {
    if(backgroundTask != UIBackgroundTaskInvalid) {
        [self endAndResetBackGroundTask];
    }
    [RSLogger logDebug:@"RNBackGroundModeManager: registerBackGroundTask: Registering for Background Mode"];
    __weak RNBackGroundModeManager *weakSelf = self;
    backgroundTask = [[UIApplication sharedApplication] beginBackgroundTaskWithExpirationHandler:^{
        RNBackGroundModeManager *strongSelf = weakSelf;
        [strongSelf endAndResetBackGroundTask];
    }];
}

- (void) endAndResetBackGroundTask {
    [[UIApplication sharedApplication] endBackgroundTask:backgroundTask];
    [self resetBackgroundTask];

}

#else

/*
 Methods useful for registering for Background Run Time after the app has been backgrounded for the platform watchOS
 */
- (void) askForAssertionWithSemaphore {
    if(self->semaphore == nil) {
        self->semaphore = dispatch_semaphore_create(0);
    } else if (!self->isSemaphoreReleased) {
        [self releaseAssertionWithSemaphore];
    }
    
    NSProcessInfo *processInfo = [NSProcessInfo processInfo];
    [processInfo performExpiringActivityWithReason:@"backgroundRunTime" usingBlock:^(BOOL expired) {
        if (expired) {
            [self releaseAssertionWithSemaphore];
            self->isSemaphoreReleased = YES;
        } else {
            [RSLogger logDebug:@"RNBackGroundModeManager: askForAssertionWithSemaphore: Asking Semaphore for Assertion to wait forever for backgroundMode"];
            self->isSemaphoreReleased = NO;
            dispatch_semaphore_wait(self->semaphore, DISPATCH_TIME_FOREVER);
        }
    }];
}

- (void) releaseAssertionWithSemaphore {
    [RSLogger logDebug:@"RNBackGroundModeManager: releaseAssertionWithSemaphore: Releasing Assertion on Semaphore for backgroundMode"];
    dispatch_semaphore_signal(self->semaphore);
}
#endif

@end
