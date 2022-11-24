import { NativeModules } from 'react-native';

const { RudderIntegrationFirebaseReactNative } = NativeModules;

if (!RudderIntegrationFirebaseReactNative) {
    throw new Error("Unable to import Rudder-Firebase native module");
}

export default RudderIntegrationFirebaseReactNative;
