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

@implementation RNRudderSprigIntegrationModuleImpl {
    BOOL _integrationRegistered;
}

- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    RudderSprigFactory *factory = [RudderSprigFactory instance];
    // Guard against re-registration on hot reload / repeated setup() calls (mirrors
    // the Android `callbacksRegistered` guard in RNRudderSprigIntegrationModuleImpl.java).
    if (!_integrationRegistered) {
        [RNRudderAnalytics addIntegration:factory];
        _integrationRegistered = YES;
    }

    // Provide the host app's root view controller so trackAndPresent surveys can show.
    // Resolve only after the VC is wired, so JS callers awaiting setup() can safely
    // fire survey-triggering events on the next tick.
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *rootVC = [self currentRootViewController];
        if (rootVC != nil) {
            [factory setViewController:rootVC];
        }
        resolve(nil);
    });
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
    // Walk the presentation chain so we hand Sprig the topmost VC. If the host
    // app already has a modal up (login sheet, paywall, etc.), presenting the
    // survey from rootViewController would throw "already presenting".
    UIViewController *vc = keyWindow.rootViewController;
    while (vc.presentedViewController != nil) {
        vc = vc.presentedViewController;
    }
    return vc;
}

@end
