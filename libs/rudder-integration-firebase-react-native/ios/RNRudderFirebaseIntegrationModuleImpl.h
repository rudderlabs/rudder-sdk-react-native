#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@interface RNRudderFirebaseIntegrationModuleImpl : NSObject

- (instancetype)initWithBridge:(RCTBridge *)bridge;
- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

@end
