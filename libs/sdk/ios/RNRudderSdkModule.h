// Thanks to this guard, we won't import this header when we build for the old architecture.

#import "RNRudderSdkModuleImpl.h"

#ifdef RCT_NEW_ARCH_ENABLED

#import "RNRudderSdkModuleSpec/RNRudderSdkModuleSpec.h"
@interface RNRudderSdkModule : NSObject <NativeRudderBridgeSpec> {

#else

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
