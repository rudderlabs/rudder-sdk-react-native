#import "RNRudderSdkModuleSpec/RNRudderSdkModuleSpec.h"

#import "RNApplicationLifeCycleManager.h"
#import "RNBackGroundModeManager.h"
#import "RNPreferenceManager.h"
#import "RNParamsConfigurator.h"
#import "RNUserSessionPlugin.h"

@interface RNRudderSdkModule : NSObject <NativeBridgeSpec> {
  RNApplicationLifeCycleManager *applicationLifeCycleManager;
  RNBackGroundModeManager *backGroundModeManager;
  RNPreferenceManager *preferenceManager;
  RNParamsConfigurator *configParams;
  BOOL initialized;
  RNUserSessionPlugin *session;
}

@end
