#import "RNRudderSprigIntegrationModuleImpl.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#import <Rudder/RSLogger.h>
#import <UIKit/UIKit.h>
#import <objc/message.h>

// Rudder-Sprig's RudderSprigFactory is a Swift class exposed to Obj-C via
// @objcMembers. We resolve it via NSClassFromString and dispatch through
// runtime selectors so this file has no compile-time dependency on
// Rudder_Sprig-Swift.h. That header lives at different paths depending on
// the consumer's use_frameworks! mode, so a direct #import would only work
// in static-libraries mode and break in any frameworks mode.

@implementation RNRudderSprigIntegrationModuleImpl {
    BOOL _integrationRegistered;
}

- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    id factory = [RNRudderSprigIntegrationModuleImpl sprigFactoryInstance];
    if (!factory) {
        [RSLogger logError:@"Sprig: RudderSprigFactory class not found at runtime; Rudder-Sprig pod missing?"];
        resolve(nil);
        return;
    }

    __weak __typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __typeof(self) strongSelf = weakSelf;
        if (!strongSelf) {
            resolve(nil);
            return;
        }

        if (!strongSelf->_integrationRegistered) {
            [RNRudderAnalytics addIntegration:factory];
            strongSelf->_integrationRegistered = YES;
        }

        UIViewController *rootVC = [RNRudderSprigIntegrationModuleImpl currentRootViewController];
        if (rootVC) {
            SEL setVC = NSSelectorFromString(@"setViewController:");
            if ([factory respondsToSelector:setVC]) {
                ((void (*)(id, SEL, UIViewController *))objc_msgSend)(factory, setVC, rootVC);
            }
        } else {
            [RSLogger logWarn:@"Sprig: no foreground key window at setup; surveys will not present until a view controller is wired."];
        }
        resolve(nil);
    });
}

+ (id)sprigFactoryInstance {
    // Swift @objcMembers classes are registered under their bare class name
    // when the module is set up via @objc(name) or via the default mangling
    // shim that CocoaPods generates for Swift pods.
    Class factoryClass = NSClassFromString(@"RudderSprigFactory");
    if (!factoryClass) {
        // Fall back to the Swift module-qualified runtime name.
        factoryClass = NSClassFromString(@"Rudder_Sprig.RudderSprigFactory");
    }
    if (!factoryClass) {
        return nil;
    }
    SEL instanceSel = NSSelectorFromString(@"instance");
    if (![factoryClass respondsToSelector:instanceSel]) {
        return nil;
    }
    return ((id (*)(Class, SEL))objc_msgSend)(factoryClass, instanceSel);
}

+ (UIViewController *)currentRootViewController {
    UIWindowScene *activeScene = nil;
    for (UIScene *scene in UIApplication.sharedApplication.connectedScenes) {
        if ([scene isKindOfClass:[UIWindowScene class]] &&
            scene.activationState == UISceneActivationStateForegroundActive) {
            activeScene = (UIWindowScene *)scene;
            break;
        }
    }

    UIWindow *keyWindow = nil;
    for (UIWindow *window in activeScene.windows) {
        if (window.isKeyWindow) {
            keyWindow = window;
            break;
        }
    }

    UIViewController *vc = keyWindow.rootViewController;
    while (vc.presentedViewController) {
        vc = vc.presentedViewController;
    }
    return vc;
}

@end
