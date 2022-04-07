#import <React/RCTBridgeModule.h>
#import <AppsFlyerLib/AppsFlyerLib.h>

@interface RudderIntegrationAppsflyerReactNative : NSObject <RCTBridgeModule, AppsFlyerLibDelegate, AppsFlyerDeepLinkDelegate>

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
