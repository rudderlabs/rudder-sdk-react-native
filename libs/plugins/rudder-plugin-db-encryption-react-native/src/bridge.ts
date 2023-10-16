import { NativeModules } from 'react-native';

const { RudderPluginDBEncryptionReactNative } = NativeModules;

if (!RudderPluginDBEncryptionReactNative) {
  throw new Error('Unable to import Rudder DBEncryption plugin');
}

export default RudderPluginDBEncryptionReactNative;
