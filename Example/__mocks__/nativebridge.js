// Note : Please do not modify the below code because it affects the result of your unit testcase,
// while you can add extra conditions to this based on your requirement
import {NativeModules} from 'react-native';

NativeModules.RNRudderSdkModule = {
  setup: jest.fn(),
  track: jest.fn(),
  screen: jest.fn(),
  identify: jest.fn(),
  reset: jest.fn(),
  putDeviceToken: jest.fn(),
  setAdvertisingId: jest.fn(),
  setAnonymousId: jest.fn()
};

NativeModules.RudderIntegrationAppcenterReactNative = {
    setup: jest.fn()
}

NativeModules.RudderIntegrationAppsflyerReactNative = {
    setup: jest.fn()
}

NativeModules.RudderIntegrationFirebaseReactNative = {
    setup: jest.fn()
}