import { NativeModules } from 'react-native';

const { RudderIntegrationSingularReactNative } = NativeModules;

if (!RudderIntegrationSingularReactNative) {
  throw new Error('Unable to import Rudder-Singular native module');
}

export default RudderIntegrationSingularReactNative;
