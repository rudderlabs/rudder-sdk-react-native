import { NativeModules } from 'react-native';

const { RudderIntegrationAppsflyerReactNative } = NativeModules;

if (!RudderIntegrationAppsflyerReactNative) {
    throw new Error("Unable to import Rudder-Appsflyer native module");
}

export default RudderIntegrationAppsflyerReactNative;
