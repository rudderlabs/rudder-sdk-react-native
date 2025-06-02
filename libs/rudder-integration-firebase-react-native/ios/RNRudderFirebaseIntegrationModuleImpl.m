#import "RNRudderFirebaseIntegrationModuleImpl.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Firebase/RudderFirebaseFactory.h>)
#import <Rudder-Firebase/RudderFirebaseFactory.h>
#else
@import Rudder_Firebase;
#endif

@implementation RNRudderFirebaseIntegrationModuleImpl {
    RCTBridge *bridge;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge {
    self = [super init];
    if (self) {
        self->bridge = bridge;
    }
    return self;
}

- (NSString *)getName {
    return @"RNRudderFirebaseIntegrationModule";
}

- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    [RNRudderAnalytics addIntegration:[RudderFirebaseFactory instance]];
    resolve(nil);
}

@end
