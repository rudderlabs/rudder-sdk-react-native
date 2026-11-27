# rudder-integration-sprig-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-sprig-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-sprig-react-native`

### iOS

Run `pod install` in your app's `ios` folder. To use Swift Package Manager instead (React Native 0.87 or later), follow [Swift Package Manager (iOS)](https://github.com/rudderlabs/rudder-sdk-react-native/tree/master/libs/sdk#swift-package-manager-ios). It needs `@rudderstack/rudder-sdk-react-native` 3.3.0 or later.

## Usage

```javascript
import sprig from '@rudderstack/rudder-integration-sprig-react-native';

const config = {
  dataPlaneUrl: TEST_DATAPLANE_URL,
  logLevel: RUDDER_LOG_LEVEL.VERBOSE,
  withFactories: [sprig],
};

await rc.setup(TEST_WRITE_KEY, config, options);
```
