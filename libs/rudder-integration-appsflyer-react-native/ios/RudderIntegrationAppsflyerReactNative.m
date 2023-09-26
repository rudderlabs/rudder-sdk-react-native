#import "RudderIntegrationAppsflyerReactNative.h"
#import <RNRudderSdk/RNRudderAnalytics.h>
#if __has_include(<Rudder-Appsflyer/RudderAppsflyerFactory.h>)
#import <Rudder-Appsflyer/RudderAppsflyerFactory.h>
#else
@import Rudder_Appsflyer;
#endif

@implementation RudderIntegrationAppsflyerReactNative

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup: (NSString*)devKey withDebug: (BOOL) isDebug withConversionDataListener: (BOOL) onInstallConversionDataListener withDeepLinkListener: (BOOL) onDeepLinkListener withAppleAppId: (NSString*) appleAppId withTimeToWaitForATTUserAuthorization: (double) timeToWaitForATTUserAuthorization resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AppsFlyerLib shared] setAppsFlyerDevKey:devKey];
    [[AppsFlyerLib shared] setAppleAppID:appleAppId];
    if(isDebug) {
        [AppsFlyerLib shared].isDebug = YES;
    }
    if(onInstallConversionDataListener) {
      [AppsFlyerLib shared].delegate = self;
    }
    if(onDeepLinkListener) {
       [AppsFlyerLib shared].deepLinkDelegate = self;
    }
    if(timeToWaitForATTUserAuthorization !=0) {
         [[AppsFlyerLib shared] waitForATTUserAuthorizationWithTimeoutInterval:timeToWaitForATTUserAuthorization];
    }
    //post notification for the deep link object that the bridge is initialized and he can handle deep link
    [[AppsFlyerAttribution shared] setRNAFBridgeReady:YES];
    [[NSNotificationCenter defaultCenter] postNotificationName:RNAFBridgeInitializedNotification object:self];
    // Register for background-foreground transitions natively instead of doing this in JavaScript
    // [[NSNotificationCenter defaultCenter] addObserver:self
    //                                             selector:@selector(sendLaunch:)
    //                                                 name:UIApplicationDidBecomeActiveNotification
    //                                             object:nil];
    [[AppsFlyerLib shared] start];
    [RNRudderAnalytics addIntegration:[RudderAppsflyerFactory instance]];
    resolve(nil);
}

RCT_EXPORT_METHOD(getAppsFlyerId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSString *appsflyerId = [AppsFlyerLib shared].getAppsFlyerUID;
    resolve(appsflyerId);
}

- (void)didResolveDeepLink:(AppsFlyerDeepLinkResult* _Nonnull) result {
    NSString *deepLinkStatus = nil;
    switch(result.status) {
        case AFSDKDeepLinkResultStatusFound:
            deepLinkStatus = @"FOUND";
            break;
        case AFSDKDeepLinkResultStatusNotFound:
            deepLinkStatus = @"NOT_FOUND";
            break;
        case AFSDKDeepLinkResultStatusFailure:
            deepLinkStatus = @"ERROR";
            break;
        default:
            [NSException raise:NSGenericException format:@"Unexpected FormatType."];
    }
        NSMutableDictionary* message = [[NSMutableDictionary alloc] initWithCapacity:5];
        message[@"status"] = ([deepLinkStatus isEqual:@"ERROR"] || [deepLinkStatus isEqual:@"NOT_FOUND"]) ? afFailure : afSuccess;
        message[@"deepLinkStatus"] = deepLinkStatus;
        message[@"type"] = afOnDeepLinking;
        message[@"isDeferred"] = result.deepLink.isDeferred ? @YES : @NO;
        if([deepLinkStatus  isEqual: @"ERROR"]){
            message[@"data"] = result.error.localizedDescription;
        }else if([deepLinkStatus  isEqual: @"NOT_FOUND"]){
            message[@"data"] = @"deep link not found";
        }else{
            message[@"data"] = result.deepLink.clickEvent;
        }

    [self performSelectorOnMainThread:@selector(handleCallback:) withObject:message waitUntilDone:NO];
}

-(void)onConversionDataSuccess:(NSDictionary*) installData {
    NSDictionary* message = @{
        @"status": afSuccess,
        @"type": afOnInstallConversionDataLoaded,
        @"data": [installData copy]
    };

    [self performSelectorOnMainThread:@selector(handleCallback:) withObject:message waitUntilDone:NO];
}


-(void)onConversionDataFail:(NSError *) _errorMessage {
    NSLog(@"[DEBUG] AppsFlyer = %@",_errorMessage.localizedDescription);
    NSDictionary* errorMessage = @{
        @"status": afFailure,
        @"type": afOnInstallConversionFailure,
        @"data": _errorMessage.localizedDescription
    };

    [self performSelectorOnMainThread:@selector(handleCallback:) withObject:errorMessage waitUntilDone:NO];

}


- (void) onAppOpenAttribution:(NSDictionary*) attributionData {
    NSDictionary* message = @{
        @"status": afSuccess,
        @"type": afOnAppOpenAttribution,
        @"data": attributionData
    };
    [self performSelectorOnMainThread:@selector(handleCallback:) withObject:message waitUntilDone:NO];
}

- (void) onAppOpenAttributionFailure:(NSError *)_errorMessage {
    NSLog(@"[DEBUG] AppsFlyer = %@",_errorMessage.localizedDescription);
    NSDictionary* errorMessage = @{
        @"status": afFailure,
        @"type": afOnAttributionFailure,
        @"data": _errorMessage.localizedDescription
    };
    [self performSelectorOnMainThread:@selector(handleCallback:) withObject:errorMessage waitUntilDone:NO];
}


-(void) handleCallback:(NSDictionary *) message {
    NSError *error;

    if ([NSJSONSerialization isValidJSONObject:message]) {
        NSData *jsonMessage = [NSJSONSerialization dataWithJSONObject:message
                                                              options:0
                                                                error:&error];
        if (jsonMessage) {
            NSString *jsonMessageStr = [[NSString alloc] initWithBytes:[jsonMessage bytes] length:[jsonMessage length] encoding:NSUTF8StringEncoding];

            NSString* status = (NSString*)[message objectForKey: @"status"];
            NSString* type = (NSString*)[message objectForKey: @"type"];

            if([status isEqualToString:afSuccess]){
                [self reportOnSuccess:jsonMessageStr type:type];
            }
            else{
                [self reportOnFailure:jsonMessageStr type:type];
            }

            NSLog(@"jsonMessageStr = %@",jsonMessageStr);
        } else {
            NSLog(@"%@",error);
        }
    }
    else{
        NSLog(@"failed to parse Response");
    }
}

-(void) reportOnFailure:(NSString *)errorMessage type:(NSString*) type {
    @try {
        [self sendEventWithName:type body:errorMessage];
    } @catch (NSException *exception) {
        NSLog(@"AppsFlyer Debug: %@", exception);
    }
}

-(void) reportOnSuccess:(NSString *)data type:(NSString*) type {
    @try {
        [self sendEventWithName:type body:data];
    } @catch (NSException *exception) {
        NSLog(@"AppsFlyer Debug: %@", exception);
    }
}

- (NSArray<NSString *> *)supportedEvents {
    return @[afOnAttributionFailure,afOnAppOpenAttribution,afOnInstallConversionFailure, afOnInstallConversionDataLoaded, afOnDeepLinking];
}

RCT_EXPORT_METHOD(updateServerUninstallToken: (NSString *)deviceToken callback:(RCTResponseSenderBlock)callback) {
    deviceToken = [deviceToken stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSMutableData *deviceTokenData= [[NSMutableData alloc] init];
    unsigned char whole_byte;
    char byte_chars[3] = {'\0','\0','\0'};
    int i;
    for (i=0; i < [deviceToken length]/2; i++) {
        byte_chars[0] = [deviceToken characterAtIndex:i*2];
        byte_chars[1] = [deviceToken characterAtIndex:i*2+1];
        whole_byte = strtol(byte_chars, NULL, 16);
        [deviceTokenData appendBytes:&whole_byte length:1];
    }
    [[AppsFlyerLib shared] registerUninstall:deviceTokenData];
    callback(@[SUCCESS]);
}

RCT_EXPORT_METHOD(setCustomerUserId: (NSString *)userId callback:(RCTResponseSenderBlock)callback) {
    [[AppsFlyerLib shared] setCustomerUserID:userId];
    callback(@[SUCCESS]);
}

RCT_EXPORT_METHOD(setOneLinkCustomDomains:(NSArray *) domains
                  successCallback :(RCTResponseSenderBlock)successCallback
                  errorCallback:(RCTResponseErrorBlock)errorCallback) {
    [[AppsFlyerLib shared] setOneLinkCustomDomains:domains];
    successCallback(@[SUCCESS]);
}

@end
