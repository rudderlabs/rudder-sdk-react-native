#import "RNRudderSdkModuleSpec/RNRudderSdkModuleSpec.h"

#import "RNApplicationLifeCycleManager.h"
#import "RNBackGroundModeManager.h"
#import "RNPreferenceManager.h"
#import "RNParamsConfigurator.h"
#import "RNUserSessionPlugin.h"

@interface RNRudderSdkModule : NSObject <NativeBridgeSpec> {
  BOOL initialized;
  RNParamsConfigurator* configParams;
  RNPreferenceManager* preferenceManager;
  RNUserSessionPlugin* session;
  RNBackGroundModeManager* backGroundModeManager;
  RNApplicationLifeCycleManager* applicationLifeCycleManager;
}

@end
