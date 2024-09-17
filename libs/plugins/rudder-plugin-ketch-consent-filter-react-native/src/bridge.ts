import { NativeModules } from 'react-native';

const { RudderPluginKetchConsentFilterReactNative } = NativeModules;

if (!RudderPluginKetchConsentFilterReactNative) {
  throw new Error('Unable to import Rudder Ketch Consent Filter plugin');
}

export default RudderPluginKetchConsentFilterReactNative;
