#import "RudderPluginDBEncryptionReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>

#if __has_include(<RudderDatabaseEncryption/RSEncryptedDatabaseProvider.h>)
#import <RudderDatabaseEncryption/RSEncryptedDatabaseProvider.h>
#else
@import RudderDatabaseEncryption;
#endif


@implementation RudderPluginDBEncryptionReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup:(NSString *)key enable:(BOOL)enable resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    RSDBEncryption *dbEncryption = [[RSDBEncryption alloc] initWithKey:key enable:enable databaseProvider:[RSEncryptedDatabaseProvider new]];
    [RNRudderAnalytics setDBEncryption:dbEncryption];
    resolve(nil);
}

@end
