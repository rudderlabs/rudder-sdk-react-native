#import "RNRudderSdkModule.h"
#import "RNRudderAnalytics.h"
#import "RSClient.h"
#import "RSConfig.h"
#import "RSLogger.h"
#import "RSOption.h"
#import "RSMessageBuilder.h"
#import <React/RCTBridge.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRudderSdkModuleSpec/RNRudderSdkModuleSpec.h"
#endif

@implementation RNRudderSdkModule

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (instancetype)init {
    self = [super init];
    if (self) {
        moduleImpl = nil;
    }
    return self;
}

- (void)setBridge:(RCTBridge *)bridge {
    _bridge = bridge;
    moduleImpl = [[RNRudderSdkModuleImpl alloc] initWithBridge:bridge];
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

// Thanks to this guard, we won't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

  // New Architecture

- (void)alias:(nonnull NSString *)newId previousId:(NSString * _Nullable)previousId options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl alias:newId previousId:previousId options:options resolver:resolve rejecter:reject];
}

- (void)clearAdvertisingId:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl clearAdvertisingId:resolve rejecter:reject];
}

- (void)endSession:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl endSession:resolve rejecter:reject];
}

- (void)flush:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl flush:resolve rejecter:reject];
}

- (void)getRudderContext:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl getRudderContext:resolve rejecter:reject];
}

- (void)getSessionId:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl getSessionId:resolve rejecter:reject];
}

- (void)group:(nonnull NSString *)groupId traits:(NSDictionary * _Nullable)traits options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl group:groupId traits:traits options:options resolver:resolve rejecter:reject];
}

- (void)identify:(nonnull NSString *)userId traits:(NSDictionary * _Nullable)traits options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl identify:userId traits:traits options:options resolver:resolve rejecter:reject];
}

- (void)optOut:(BOOL)optOut resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl optOut:optOut resolver:resolve rejecter:reject];
}

- (void)putAdvertisingId:(nonnull NSString *)id resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl putAdvertisingId:id resolver:resolve rejecter:reject];
}

- (void)putAnonymousId:(nonnull NSString *)id resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl putAnonymousId:id resolver:resolve rejecter:reject];
}

- (void)putDeviceToken:(nonnull NSString *)token resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl putDeviceToken:token resolver:resolve rejecter:reject];
}

- (void)reset:(BOOL)clearAnonymousId resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl reset:clearAnonymousId resolver:resolve rejecter:reject];
}

- (void)screen:(nonnull NSString *)name properties:(NSDictionary * _Nullable)properties options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl screen:name properties:properties options:options resolver:resolve rejecter:reject];
}

- (void)setup:(NSDictionary * _Nullable)configuration options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl setup:configuration options:options resolver:resolve rejecter:reject];
}

- (void)startSession:(nonnull NSString *)sessionId resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl startSession:sessionId resolver:resolve rejecter:reject];
}

- (void)track:(nonnull NSString *)event properties:(NSDictionary * _Nullable)properties options:(NSDictionary * _Nullable)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [moduleImpl track:event properties:properties options:options resolver:resolve rejecter:reject];
}

- (void)registerCallback:(nonnull NSString *)integrationName callback:(nonnull RCTResponseSenderBlock)callback { 
  [moduleImpl registerCallback:integrationName callback:callback];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeRudderBridgeSpecJSI>(params);
}

#else

  // Legacy Architecture

RCT_EXPORT_METHOD(setup:(NSDictionary*)config options:(NSDictionary*) _options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl setup:config options:_options resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(track:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl track:_event properties:_properties options:_options resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(screen:(NSString*)_event properties:(NSDictionary*)_properties options:(NSDictionary*)_options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl screen:_event properties:_properties options:_options resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(identify:(NSString*)_userId traits:(NSDictionary*)_traits options:(NSDictionary*)_options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl identify:_userId traits:_traits options:_options resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(alias:(NSString*)_newId previousId:(NSString*)_previousId options:(NSDictionary*)_options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl alias:_newId previousId:_previousId options:_options resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(group:(NSString*)_groupId traits:(NSDictionary*)_traits options:(NSDictionary*)_options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl group:_groupId traits:_traits options:_options resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(putDeviceToken:(NSString*)token resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl putDeviceToken:token resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(reset:(BOOL)clearAnonymousId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl reset:clearAnonymousId resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(flush:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl flush:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(optOut:(BOOL)optOut resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl optOut:optOut resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(putAdvertisingId:(NSString*)id resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [moduleImpl putAdvertisingId:id resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(clearAdvertisingId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [moduleImpl clearAdvertisingId:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(putAnonymousId:(NSString*)id resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [moduleImpl putAnonymousId:id resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(registerCallback:(NSString *)name callback: (RCTResponseSenderBlock)callback)
{
  [moduleImpl registerCallback:name callback:callback];
}

RCT_EXPORT_METHOD(getRudderContext:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  [moduleImpl getRudderContext:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(startSession:(NSString *)sessionId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [moduleImpl startSession:sessionId resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(endSession:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [moduleImpl endSession:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(getSessionId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [moduleImpl getSessionId:resolve rejecter:reject];
}

#endif

@end
