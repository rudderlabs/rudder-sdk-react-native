import { NativeModules } from 'react-native';

const { RudderIntegrationCleverTapReactNative } = NativeModules;

if (!RudderIntegrationCleverTapReactNative) {
    throw new Error("Unable to import Rudder-CleverTap native module");
}

export default RudderIntegrationCleverTapReactNative;
