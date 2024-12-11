import { NativeModules } from 'react-native';

const { RudderPluginOnetrustConsentFilterReactNative } = NativeModules;

if (!RudderPluginOnetrustConsentFilterReactNative) {
  throw new Error('Unable to import Rudder Onetrust Consent Filter plugin');
}

export default RudderPluginOnetrustConsentFilterReactNative;
