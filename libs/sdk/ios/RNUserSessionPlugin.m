//
//  RNUserSessionPlugin.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 03/08/23.
//

#import <Foundation/Foundation.h>
#import "RNUserSessionPlugin.h"

@implementation RNUserSessionPlugin

-(instancetype) initWithAutomaticSessionTrackingStatus:(BOOL)automaticSessionTrackingStatus withLifecycleEventsTrackingStatus:(BOOL)lifecycleEventsTrackingStatus withSessionTimeout:(long)sessionTimeout  {
    self = [super init];
    if(self){
        self->sessionTimeout = sessionTimeout;
        self->rudderClient = [RSClient getInstance];
        self->sessionParams = [[RNSessionTrackingParams alloc] init];
        self->isAutomaticSessionTrackingEnabled = automaticSessionTrackingStatus && lifecycleEventsTrackingStatus;
    }
    return self;
}

- (void)handleSessionTracking {
    if (!self->isAutomaticSessionTrackingEnabled) {
        [self endSessionIfManualSessionInactivePreviously];
    } else {
        [self handleAutomaticSessionTracking];
    }
}

- (void)endSessionIfManualSessionInactivePreviously {
    if (![self->sessionParams wasManualSessionActive]) {
        [RSLogger logVerbose:@"RNUserSessionPlugin: As previously manual session tracking was not enabled. Hence clear the session"];
        [self endSession];
    }
}

-(void)handleAutomaticSessionTracking {
    if ([self->sessionParams wasManualSessionActive] || [self->sessionParams wasSessionTrackingDisabled]) {
        [RSLogger logVerbose:@"RNUserSessionPlugin: As previously manual session tracking was enabled or session tracking was disabled. Hence start a new session"];
        [self startSession];
    } else {
        [self startNewSessionIfCurrentIsExpired];
    }
    [self enableAutomaticSessionParams];
}

- (void)startNewSessionIfCurrentIsExpired {
    if ([self->sessionParams isAutomaticSessionActive]) {
        if ([self isSessionExpired]) {
            [RSLogger logVerbose:@"RNUserSessionPlugin: previous session is expired"];
            [self startSession];
        }
    }
}

-(BOOL)isSessionExpired {
    long currentTime = [RSUtils getTimeStampLong];
    long lastEventTimestamp = [self->sessionParams.lastEventTimeStamp longValue];
    long timeDifference;
    @synchronized (self) {
        timeDifference = labs(currentTime - lastEventTimestamp);
    }
    if (timeDifference >= (self->sessionTimeout/1000)) {
        return YES;
    }
    return NO;
}

- (void)saveEventTimestamp {
    [self->sessionParams saveEventTimestamp];
}

- (void)startSession {
    if (self->rudderClient != nil) {
        [self->rudderClient startSession];
        [RSLogger logVerbose:@"RNUserSessionPlugin: starting new session"];
    }
}

- (void)startSession:(long)sessionId {
    if (self->rudderClient != nil) {
        [self->rudderClient startSession:sessionId];
        [RSLogger logVerbose:[NSString stringWithFormat:@"RNUserSessionPlugin: starting new session with sessionId: %ld", sessionId]];
    }
}

- (void)endSession {
    if (self->rudderClient != nil) {
        [self->rudderClient endSession];
        [self disableSessionParams];
        [RSLogger logVerbose:@"RNUserSessionPlugin: ending session"];
    }
}

- (NSNumber * _Nullable)getSessionId {
    return (self->rudderClient != nil) ? self->rudderClient.sessionId : nil;
}

-(void)enableAutomaticSessionParams {
    [self->sessionParams enableSessionParams:YES manual:NO];
}

-(void)enableManualSessionParams {
    [self->sessionParams enableSessionParams:NO manual:YES];
}

-(void)disableSessionParams {
    [self->sessionParams enableSessionParams:NO manual:NO];
}

@end
