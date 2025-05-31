#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import "RSOption.h"

@interface RNRudderSdkModuleImpl : NSObject

- (instancetype)initWithBridge:(RCTBridge *)bridge;

- (NSString *)getName;
- (BOOL)isRudderClientInitializedAndReady;

// Main methods
- (void)setup:(NSDictionary*)config options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)track:(NSString*)event properties:(NSDictionary*)properties options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)screen:(NSString*)name properties:(NSDictionary*)properties options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)identify:(NSString*)userId traits:(NSDictionary*)traits options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)alias:(NSString*)newId previousId:(NSString*)previousId options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)group:(NSString*)groupId traits:(NSDictionary*)traits options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)putDeviceToken:(NSString*)token resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)reset:(BOOL)clearAnonymousId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)flush:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)optOut:(BOOL)optOut resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)putAdvertisingId:(NSString*)id resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)clearAdvertisingId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)putAnonymousId:(NSString*)id resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)registerCallback:(NSString*)name callback:(RCTResponseSenderBlock)callback;

- (void)getRudderContext:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)startSession:(NSString*)sessionId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)endSession:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (void)getSessionId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

- (RSOption*)getRudderOptionsObject:(NSDictionary*)optionsDict;

@end
