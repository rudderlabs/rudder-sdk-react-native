import { NativeModules } from 'react-native';

const { RudderIntegrationFacebookReactNative } = NativeModules;

if (!RudderIntegrationFacebookReactNative) {
  throw new Error('Unable to import Rudder-Facebook native module');
}

export default RudderIntegrationFacebookReactNative;
