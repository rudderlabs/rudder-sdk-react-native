#import "RNRudderSprigIntegrationModuleImpl.h"
#import <UIKit/UIKit.h>
#import <RNRudderSdk/RNRudderAnalytics.h>

// Forward-declare the Swift `RudderSprigFactory` rather than importing
// <Rudder_Sprig/Rudder_Sprig-Swift.h>. The generated Swift header transitively
// pulls in the Rudder umbrella, which defines `NSString *const SDK_VERSION`
// inside RSVersion.h — a non-extern global that produces a duplicate-symbol
// link error against libRudder.a's RSConstants.o.
//
// The objc_runtime_name attribute must match the mangled name Swift emits
// (`_TtC12Rudder_Sprig18RudderSprigFactory`) so the ObjC linker resolves
// `[RudderSprigFactory instance]` to the Swift class symbol.
__attribute__((objc_runtime_name("_TtC12Rudder_Sprig18RudderSprigFactory")))
@interface RudderSprigFactory : NSObject <RSIntegrationFactory>
+ (RudderSprigFactory *)instance;
- (void)setViewController:(UIViewController *)viewController;
@end

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
