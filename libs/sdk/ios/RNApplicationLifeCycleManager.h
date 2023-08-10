//
//  RNApplicationLifeCycleManager.h
//  Pods
//
//  Created by Abhishek Pandey on 20/07/23.
//

#ifndef RNApplicationLifeCycleManager_h
#define RNApplicationLifeCycleManager_h

#import <Foundation/Foundation.h>
#import "RNPreferenceManager.h"
#import "RNBackGroundModeManager.h"
#import "RSUtils.h"
#import "RNParamsConfigurator.h"
#import "RNUserSessionPlugin.h"

@interface RNApplicationLifeCycleManager : NSObject {
    RNPreferenceManager* preferenceManager;
    RNBackGroundModeManager* backGroundModeManager;
    BOOL trackLifecycleEvents;
    BOOL firstForeGround;
    RNUserSessionPlugin *session;
}

- (instancetype)initWithTrackLifecycleEvents:(BOOL)trackLifecycleEvents andBackGroundModeManager:(RNBackGroundModeManager *)backGroundModeManager withLaunchOptions:(id)launchOptions withSessionPlugin:(RNUserSessionPlugin *)session;
- (void) trackApplicationLifeCycle;
- (void) prepareScreenRecorder;

@end



#endif /* RNApplicationLifeCycleManager_h */
