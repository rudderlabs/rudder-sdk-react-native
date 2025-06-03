#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@interface RNRudderFirebaseIntegrationModuleImpl : NSObject

- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

@end
