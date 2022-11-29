import { NativeModules } from 'react-native';

const { RudderIntegrationAmplitudeReactNative } = NativeModules;

if (!RudderIntegrationAmplitudeReactNative) {
  throw new Error('Unable to import Rudder-Amplitude native module');
}

export default RudderIntegrationAmplitudeReactNative;
