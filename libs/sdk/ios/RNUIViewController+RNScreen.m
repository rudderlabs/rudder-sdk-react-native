//
//  RNUIViewController+RNScreen.m
//  RNRudderSdk
//
//  Created by Abhishek Pandey on 07/08/23.
//

#import <Foundation/Foundation.h>
#import "RNUIViewController+RNScreen.h"
#import "RSLogger.h"
#import "RSClient.h"
#import <objc/runtime.h>

#if TARGET_OS_IOS || TARGET_OS_TV

@implementation UIViewController (RNScreen)

RNUserSessionPlugin *session;

+(void)initSessionPlugin:(RNUserSessionPlugin *)sessionPlugin {
    session = sessionPlugin;
}

+(void)rudder_rn_swizzleView {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        Class class = [self class];
        
        SEL originalSelector = @selector(viewDidAppear:);
        SEL swizzledSelector = @selector(rudder_rn_viewDidAppear:);
        
        Method originalMethod = class_getInstanceMethod(class, originalSelector);
        Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);
        
        BOOL didAddMethod =
        class_addMethod(class,
                        originalSelector,
                        method_getImplementation(swizzledMethod),
                        method_getTypeEncoding(swizzledMethod));
        
        if (didAddMethod) {
            class_replaceMethod(class,
                                swizzledSelector,
                                method_getImplementation(originalMethod),
                                method_getTypeEncoding(originalMethod));
        } else {
            method_exchangeImplementations(originalMethod, swizzledMethod);
        }
    });
}

- (void) rudder_rn_viewDidAppear: (BOOL) animated {
    NSString *name = [[self class] description];
    if (name == nil) {
        [RSLogger logWarn:@"Couldn't get the screen name"];
        name = @"Unknown";
    }
    name = [name stringByReplacingOccurrencesOfString:@"ViewController" withString:@""];
    [session saveEventTimestamp];
    [[RSClient sharedInstance] screen:name properties:@{@"automatic": [[NSNumber alloc] initWithBool:YES], @"name": name}];
    
    [self rudder_rn_viewDidAppear:animated];
}

@end

#endif
