# What is RudderStack?

[RudderStack](https://rudderstack.com/) is a **customer data pipeline** tool for collecting, routing and processing data from your websites, apps, cloud tools, and data warehouse.

More information on RudderStack can be found [here](https://github.com/rudderlabs/rudder-server).

## RudderStack React Native SDK

The RudderStack React Native SDK allows you to track event data from your app. It can be easily integrated into your React Native application. After integrating this SDK, you will also send the event data to your preferred analytics destination/s, such as Google Analytics, Amplitude, and more.

## Getting Started with React Native SDK

1. Add the SDK to your React Native application with the following command:

```
yarn add @rudderstack/rudder-sdk-react-native
```

2. For Android: Make sure you have added `mavenCentral()` as a repository in your project level `build.gradle` file, as shown below:

```
buildscript {
    repositories {
        mavenCentral()
    }
}
allprojects {
    repositories {
        mavenCentral()
    }
}
```

3. For iOS: Navigate to the `ios` folder of your application and install all the dependencies with:

```
pod install
```

## Import `RudderClient`

Add the below line to `import` the rudderClient.

```
import rudderClient, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';
```

## Initialize `RudderClient`

Add the following code in your application:

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

## Device Tokens

You can use the `putDeviceToken` method to pass your Android and iOS device tokens.

`putDeviceToken` accepts two `string` arguments :

- `androidToken` : Your Android device token
- `iOSToken` : Your iOS device token

An example usage is as shown below:

```
rudderClient.putDeviceToken("<Your Android device token>", "<Your iOS device token>");
```

## AdvertisingId

You can use the `setAdvertisingId` method to pass your Android and iOS AAID and IDFA respectively.
`setAdvertisingId` accepts two `string` arguments :

- `androidId` : Your Android advertisingId (AAID)
- `iOSId` : Your iOS advertisingId (IDFA)

Example Usage:

```
rudderClient.setAdvertisingId(AAID, IDFA);
```

## AnonymousId

You can use the `setAnonymousId` method to pass your `anonymousId` and the SDK will use that instead of the `deviceId`.
`setAnonymousId` accepts one `string` argument:

- `id` : Your anonymousId

Example Usage:

```
rudderClient.setAnonymousId(ANONYMOUS_ID);
```

## Database Encryption

To encrypt the database first add the below line to `import` the rudderClient and DBEncryption.

```
import rudderClient, { RUDDER_LOG_LEVEL, DBEncryption } from '@rudderstack/rudder-sdk-react-native';
```

Then open your `app/build.gradle` and add the dependency under `dependencies` as shown below:

```
//sql-cipher
implementation "net.zetetic:android-database-sqlcipher:4.5.4"
implementation "androidx.sqlite:sqlite:2.3.1"
```

Add the following code in your application:

```
const dbEncryption = new DBEncryption('versys', true);

const config = {
  dataPlaneUrl: <DATA_PLANE_URL>,
  trackAppLifecycleEvents: true,
  logLevel: RUDDER_LOG_LEVEL.DEBUG,
  dbEncryption: dbEncryption,
};

await rudderClient.setup(<WRITE_KEY>, config);
```

This feature is supported starting from version `1.9.0` of the SDK.

For more details, check out our [documentation](https://www.rudderstack.com/docs/sources/event-streams/sdks/rudderstack-react-native-sdk).

## Contact Us

If you come across any issues while configuring or using RudderStack React Native SDK, please feel free to start a conversation on our [Slack](https://resources.rudderstack.com/join-rudderstack-slack) channel. We will be happy to help you.
