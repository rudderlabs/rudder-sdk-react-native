
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import "RNApplicationLifeCycleManager.h"
#import "RNBackGroundModeManager.h"
#import "RNPreferenceManager.h"
#import "RNParamsConfigurator.h"
#import "RNUserSessionPlugin.h"

@interface RNRudderSdkModule : NSObject <RCTBridgeModule> {
    RNApplicationLifeCycleManager *applicationLifeCycleManager;
    RNBackGroundModeManager *backGroundModeManager;
    RNPreferenceManager *preferenceManager;
    RNParamsConfigurator *configParams;
    BOOL initialized;
    RNUserSessionPlugin *session;
}


@end
  
