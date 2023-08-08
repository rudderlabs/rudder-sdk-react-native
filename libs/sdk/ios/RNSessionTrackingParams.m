//
//  RNSessionTrackingParams.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 03/08/23.
//

#import <Foundation/Foundation.h>
#import "RNSessionTrackingParams.h"

@implementation RNSessionTrackingParams

-(instancetype)init {
    self = [super init];
    if(self){
        self->preferenceManager = [RNPreferenceManager getInstance];
        [self refreshSessionTrackingParams];
        self.lastEventTimeStamp = [self->preferenceManager getLastEventTimeStamp];
    }
    return self;
}

- (void)refreshSessionTrackingParams {
    self->isAutomaticSessionTrackingStatus = [preferenceManager getAutomaticSessionTrackingStatus];
    self->isManualSessionTrackingStatus = [preferenceManager getManualSessionTrackingStatus];
}

- (BOOL)isAutomaticSessionActive {
    return self->isAutomaticSessionTrackingStatus;
}

- (BOOL)wasManualSessionActive {
    return self->isManualSessionTrackingStatus;
}

- (BOOL)wasSessionTrackingDisabled {
    return !self->isAutomaticSessionTrackingStatus && !self->isManualSessionTrackingStatus;
}

- (void)saveEventTimestamp {
    self.lastEventTimeStamp = [[NSNumber alloc] initWithLong:[RSUtils getTimeStampLong]];
    [preferenceManager saveLastEventTimeStamp:self.lastEventTimeStamp];
}

- (void)enableSessionParams:(BOOL)automatic manual:(BOOL)manual {
    [preferenceManager saveAutomaticSessionTrackingStatus:automatic];
    [preferenceManager saveManualSessionTrackingStatus:manual];
    [self refreshSessionTrackingParams];
}

@end
