//
//  RNWKInterfaceController+RNScreen.h
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 07/08/23.
//

#ifndef RNWKInterfaceController_RNScreen_h
#define RNWKInterfaceController_RNScreen_h

#import "RNUserSessionPlugin.h"
#include <TargetConditionals.h>
#if TARGET_OS_WATCH
#import <WatchKit/WatchKit.h>

@interface WKInterfaceController (RNScreen)

+(void)initSessionPlugin:(RNUserSessionPlugin *)sessionPlugin;
+ (void) rudder_rn_swizzleView;

@end
#endif

#endif /* RNWKInterfaceController_RNScreen_h */
