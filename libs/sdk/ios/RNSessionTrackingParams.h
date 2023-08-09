//
//  RNSessionTrackingParams.h
//  Pods
//
//  Created by Abhishek Pandey on 03/08/23.
//

#ifndef RNSessionTrackingParams_h
#define RNSessionTrackingParams_h
#import "RNPreferenceManager.h"
#import "RSUtils.h"

@interface RNSessionTrackingParams : NSObject {
    BOOL isAutomaticSessionTrackingStatus;
    BOOL isManualSessionTrackingStatus;
    RNPreferenceManager *preferenceManager;
}

@property (nonatomic, strong) NSNumber *lastEventTimeStamp;

-(void)refreshSessionTrackingParams;
-(BOOL)isAutomaticSessionActive;
-(BOOL)wasManualSessionActive;
-(BOOL)wasSessionTrackingDisabled;
-(void)saveEventTimestamp;
-(void)enableSessionParams:(BOOL)automatic manual:(BOOL)manual;

@end

#endif /* RNSessionTrackingParams_h */
