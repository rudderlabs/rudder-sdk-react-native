//
//  RNUserSessionPlugin.h
//  Pods
//
//  Created by Abhishek Pandey on 03/08/23.
//

#ifndef RNUserSessionPlugin_h
#define RNUserSessionPlugin_h
#import "RSClient.h"
#import "RNSessionTrackingParams.h"
#import "RSLogger.h"
#import "RSUtils.h"

@interface RNUserSessionPlugin : NSObject {
    long sessionTimeout;
    RSClient *rudderClient;
    RNSessionTrackingParams *sessionParams;
    BOOL isAutomaticSessionTrackingEnabled;
}

-(instancetype)initWithAutomaticSessionTrackingStatus:(BOOL)automaticSessionTrackingStatus withLifecycleEventsTrackingStatus:(BOOL)lifecycleEventsTrackingStatus withSessionTimeout:(long)sessionTimeou;

-(void)handleSessionTracking;
-(void)startNewSessionIfCurrentIsExpired;
-(void)saveEventTimestamp;
-(void)startSession;
-(void)startSession:(long)sessionId;
-(void)endSession;
-(void)enableManualSessionParams;

@end

#endif /* RNUserSessionPlugin_h */
