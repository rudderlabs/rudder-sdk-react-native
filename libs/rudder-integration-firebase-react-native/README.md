# rudder-integration-firebase-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-firebase-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-firebase-react-native`

## Usage

```javascript
import firebase from '@rudderstack/rudder-integration-firebase-react-native';

const config = {
  dataPlaneUrl: TEST_DATAPLANE_URL,
  logLevel: RUDDER_LOG_LEVEL.VERBOSE,
  withFactories: [firebase],
};

await rc.setup(TEST_WRITE_KEY, config, options);
```
