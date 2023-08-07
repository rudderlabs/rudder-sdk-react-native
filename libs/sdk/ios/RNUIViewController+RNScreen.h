//
//  RNUIViewController+RNScreen.h
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 07/08/23.
//

#ifndef UIViewController_RNScreen_h
#define UIViewController_RNScreen_h
#import "RNUserSessionPlugin.h"

#if TARGET_OS_IOS || TARGET_OS_TV

#import <UIKit/UIKit.h>

@interface UIViewController (RNScreen)

+(void)initSessionPlugin:(RNUserSessionPlugin *)sessionPlugin;
+(void) rudder_rn_swizzleView;

@end

#endif

#endif /* RNUIViewController_RNScreen_h */
