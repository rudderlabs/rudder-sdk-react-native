#import "RNRudderSprigIntegrationModuleImpl.h"
#import <UIKit/UIKit.h>
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder_Sprig/Rudder_Sprig-Swift.h>)
#import <Rudder_Sprig/Rudder_Sprig-Swift.h>
#else
@import Rudder_Sprig;
#endif

@implementation RNRudderSprigIntegrationModuleImpl

- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    RudderSprigFactory *factory = [RudderSprigFactory instance];
    [RNRudderAnalytics addIntegration:factory];

    // Provide the host app's root view controller so trackAndPresent surveys can show.
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *rootVC = [self currentRootViewController];
        if (rootVC != nil) {
            [factory setViewController:rootVC];
        }
    });

    resolve(nil);
}

- (UIViewController *)currentRootViewController {
    UIWindow *keyWindow = nil;
    NSSet<UIScene *> *scenes = [[UIApplication sharedApplication] connectedScenes];
    for (UIScene *scene in scenes) {
        if (scene.activationState != UISceneActivationStateForegroundActive) {
            continue;
        }
        if (![scene isKindOfClass:[UIWindowScene class]]) {
            continue;
        }
        UIWindowScene *windowScene = (UIWindowScene *)scene;
        for (UIWindow *window in windowScene.windows) {
            if (window.isKeyWindow) {
                keyWindow = window;
                break;
            }
        }
        if (keyWindow != nil) {
            break;
        }
    }
    return keyWindow.rootViewController;
}

@end
