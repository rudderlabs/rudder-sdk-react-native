#import "RNRudderFirebaseIntegrationModule.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRudderFirebaseIntegrationModuleSpec/RNRudderFirebaseIntegrationModuleSpec.h"
#endif

@implementation RNRudderFirebaseIntegrationModule

RCT_EXPORT_MODULE();

- (instancetype)init {
  self = [super init];
  if (self) {
    moduleImpl = [[RNRudderFirebaseIntegrationModuleImpl alloc] init];
  }
  return self;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

  // Thanks to this guard, we won't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

  // New Architecture
- (void)setup:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl setup:resolve rejecter:reject];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeFirebaseBridgeSpecJSI>(params);
}

#else

  // Legacy Architecture
RCT_EXPORT_METHOD(setup:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl setup:resolve rejecter:reject];
}

#endif

@end
