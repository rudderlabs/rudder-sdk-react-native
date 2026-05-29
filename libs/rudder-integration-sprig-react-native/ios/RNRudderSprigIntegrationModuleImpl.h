#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@interface RNRudderSprigIntegrationModuleImpl : NSObject

- (void)setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

@end
