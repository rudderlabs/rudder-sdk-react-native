import { NativeModules } from 'react-native';

const { RudderIntegrationBrazeReactNative } = NativeModules;

if (!RudderIntegrationBrazeReactNative) {
    throw new Error("Unable to import Rudder-Braze native module");
}

export default RudderIntegrationBrazeReactNative;
