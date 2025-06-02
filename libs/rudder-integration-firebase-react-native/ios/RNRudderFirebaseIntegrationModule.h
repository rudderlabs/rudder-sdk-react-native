#import "RNRudderFirebaseIntegrationModuleImpl.h"

  // Thanks to this guard, we won't import this header when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

  // New Architecture
#import "RNRudderFirebaseIntegrationModuleSpec/RNRudderFirebaseIntegrationModuleSpec.h"
@interface RNRudderFirebaseIntegrationModule : NSObject <NativeFirebaseBridgeSpec> {

#else

  // Legacy Architecture
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNRudderFirebaseIntegrationModule : NSObject <RCTBridgeModule> {

#endif

  RNRudderFirebaseIntegrationModuleImpl *moduleImpl;
}

@end
