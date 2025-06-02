#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@interface RNRudderFirebaseIntegrationModuleImpl : NSObject

- (instancetype)initWithBridge:(RCTBridge *)bridge;
- (NSString *)getName;
- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

@end
