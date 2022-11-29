import { NativeModules } from 'react-native';

const { RudderIntegrationMoengageReactNative } = NativeModules;

if (!RudderIntegrationMoengageReactNative) {
  throw new Error('Unable to import Rudder-Moengage native module');
}

export default RudderIntegrationMoengageReactNative;
