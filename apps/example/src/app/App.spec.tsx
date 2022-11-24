import React from 'react';
import { render } from '@testing-library/react-native';
import App from './App';

jest.mock("react-native", () => {
  // use original implementation, which comes with mocks out of the box
  const RN = jest.requireActual("react-native");

  // mock modules/components created by assigning to NativeModules
  RN.NativeModules.RNRudderSdkModule = {
    setup: jest.fn(),
    track: jest.fn(),
    screen: jest.fn(),
    identify: jest.fn(),
    reset: jest.fn(),
    putDeviceToken: jest.fn(),
    setAdvertisingId: jest.fn(),
    setAnonymousId: jest.fn()
  };
  RN.NativeModules.RudderIntegrationAppcenterReactNative = {
    setup: jest.fn()
  };
  RN.NativeModules.RudderIntegrationAppsflyerReactNative = {
    setup: jest.fn(),
    setOneLinkCustomDomains: jest.fn()
  };
  RN.NativeModules.RudderIntegrationCleverTapReactNative = {
    setup: jest.fn()
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

test('renders correctly', () => {
  const { getByTestId } = render(<App />);
  expect(getByTestId('init_btn')).toHaveTextContent('Initialization');
});
