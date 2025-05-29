#import "RNRudderSdkModuleImpl.h"

  // Thanks to this guard, we won't import this header when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

  // New Architecture
#import "RNRudderSdkModuleSpec/RNRudderSdkModuleSpec.h"
@interface RNRudderSdkModule : NSObject <NativeRudderBridgeSpec> {

#else

  // Legacy Archietecture
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNRudderSdkModule : NSObject <RCTBridgeModule> {

#endif

  RNRudderSdkModuleImpl *moduleImpl;
}

@end
