@class RNRudderSprigIntegrationModuleImpl;

  // Thanks to this guard, we won't import this header when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

  // New Architecture
#import "RNRudderSprigIntegrationModuleSpec/RNRudderSprigIntegrationModuleSpec.h"
@interface RNRudderSprigIntegrationModule : NSObject <NativeSprigBridgeSpec> {

#else

  // Legacy Architecture
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNRudderSprigIntegrationModule : NSObject <RCTBridgeModule> {

#endif

  @private
  RNRudderSprigIntegrationModuleImpl *moduleImpl;
}

@end
