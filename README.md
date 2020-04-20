# What is Rudder?

**Short answer:** 
Rudder is an open-source Segment alternative written in Go, built for the enterprise. .

**Long answer:** 
Rudder is a platform for collecting, storing and routing customer event data to dozens of tools. Rudder is open-source, can run in your cloud environment (AWS, GCP, Azure or even your data-centre) and provides a powerful transformation framework to process your event data on the fly.

Released under [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Getting Started with ReactNative SDK

1. Add the sdk to your ReactNative application with
```
yarn add @rudderstack/rudder-sdk-react-native
```
2. For Android: Navigate to the `android` folder of your application and add the following to your project's `build.gradle` file: 
```
maven { 
    url  "https://dl.bintray.com/rudderstack/rudderstack" 
}
```
3. For iOs: Navigate to the `ios` folder of your application and install all the dependencies with:
```
pod install
```

## Import `RudderClient`
Add the below line to `import` the rudderClient.
```
import rudderClient, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';
```

## Initialize ```RudderClient```
Somewhere in your Application, add the following code
```
const config = {
  dataPlaneUrl: <DATA_PLANE_URL>,
  trackAppLifecycleEvents: true,
  logLevel: RUDDER_LOG_LEVEL.DEBUG
};
await rudderClient.setup(<WRITE_KEY>, config);
```

## Send Events
```
rudderClient.track("simple_track_event", {
  "key1":"val1",
  "key2":{
    "child_key1":"child_val1"
  }
});
```

For more detailed documentation check [the documentation page](https://docs.rudderstack.com/sdk-integration-guide/getting-started-with-reactnative-sdk).

## Contact Us
If you come across any issues while configuring or using RudderStack, please feel free to [contact us](https://rudderstack.com/contact/) or start a conversation on our [Discord](https://discordapp.com/invite/xNEdEGw) channel. We will be happy to help you.
