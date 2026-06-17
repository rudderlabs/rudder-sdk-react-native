# rudder-integration-sprig-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-sprig-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-sprig-react-native`

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
