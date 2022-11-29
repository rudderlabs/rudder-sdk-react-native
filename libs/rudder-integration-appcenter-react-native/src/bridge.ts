import { NativeModules } from 'react-native';

const { RudderIntegrationAppcenterReactNative } = NativeModules;

if (!RudderIntegrationAppcenterReactNative) {
  throw new Error('Unable to import Rudder-Appcenter native module');
}

export default RudderIntegrationAppcenterReactNative;
