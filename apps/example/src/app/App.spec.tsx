import React from 'react';
import { render } from '@testing-library/react-native';
import App from './App';
import { assert } from 'console';

jest.mock('@rudderstack/rudder-sdk-react-native', () => jest.fn());

// Mock ScrollView component
jest.mock('react-native', () => {
  const ActualReactNative = jest.requireActual('react-native');
  return {
    ...ActualReactNative,
    ScrollView: 'ScrollView',
  };
});

jest.mock('react-native', () => {
  // use original implementation, which comes with mocks out of the box
  const RN = jest.requireActual('react-native');

  // mock modules/components created by assigning to NativeModules
  RN.NativeModules.RNRudderSdkModule = {
    setup: jest.fn(),
    track: jest.fn(),
    screen: jest.fn(),
    identify: jest.fn(),
    reset: jest.fn(),
    putDeviceToken: jest.fn(),
    setAdvertisingId: jest.fn(),
    setAnonymousId: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationAppcenterReactNative = {
    setup: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationAppsflyerReactNative = {
    setup: jest.fn(),
    setOneLinkCustomDomains: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationCleverTapReactNative = {
    setup: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationAmplitudeReactNative = {
    setup: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationBrazeReactNative = {
    setup: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationFirebaseReactNative = {
    setup: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationMoengageReactNative = {
    setup: jest.fn(),
  };
  RN.NativeModules.RudderIntegrationSingularReactNative = {
    setup: jest.fn(),
  };

  // mock modules created through UIManager
  // RN.UIManager.getViewManagerConfig = name => {
  //   if (name === "SomeNativeModule") {
  //     return {
  //       someMethod: jest.fn()
  //     }
  //   }
  //   return {};
  // };
  return RN;
});

test('demo test', () => {
  assert(true);
});
