#if __has_include(<React/RCTBridgeModule.h>) //ver >= 0.40
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTEventDispatcher.h>
#else //ver < 0.40
#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#import "AppsFlyerAttribution.h"
#endif

#import <AppsFlyerLib/AppsFlyerLib.h>

@interface RudderIntegrationAppsflyerReactNative : RCTEventEmitter <RCTBridgeModule, AppsFlyerLibDelegate, AppsFlyerDeepLinkDelegate>

  #define afConversionData                @"onInstallConversionDataListener"
  #define afOnInstallConversionData       @"onInstallConversionData"
  #define afSuccess                       @"success"
  #define afFailure                       @"failure"
  #define afOnAttributionFailure          @"onAttributionFailure"
  #define afOnAppOpenAttribution          @"onAppOpenAttribution"
  #define afOnInstallConversionFailure    @"onInstallConversionFailure"
  #define afOnInstallConversionDataLoaded @"onInstallConversionDataLoaded"
  #define afDeepLink                      @"onDeepLinkListener"
  #define afOnDeepLinking                 @"onDeepLinking"

@end
