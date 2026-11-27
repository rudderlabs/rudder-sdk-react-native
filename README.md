<p align="center">
  <a href="https://rudderstack.com/">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://cdn.rudderlabs.com/brand/logo_watermark_dark.png">
      <img alt="RudderStack" width="512" src="https://cdn.rudderlabs.com/brand/logo_watermark_light.png">
    </picture>
  </a>
</p>

# What is RudderStack?

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/rudderlabs/rudder-sdk-react-native)

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

To use Swift Package Manager instead of CocoaPods, see [Swift Package Manager (iOS)](#swift-package-manager-ios).

## Swift Package Manager (iOS)

React Native 0.87 and later can link native libraries with Swift Package Manager (SPM) instead of CocoaPods. The SDK supports SPM from version 3.3.0. Each RudderStack integration supports SPM from the version below:

| Package                                                  | First version with SPM |
| -------------------------------------------------------- | ---------------------- |
| `@rudderstack/rudder-sdk-react-native`                   | 3.3.0                  |
| `@rudderstack/rudder-integration-amplitude-react-native` | 1.5.0                  |
| `@rudderstack/rudder-integration-appcenter-react-native` | 1.5.0                  |
| `@rudderstack/rudder-integration-appsflyer-react-native` | 1.10.0                 |
| `@rudderstack/rudder-integration-braze-react-native`     | 2.7.0                  |
| `@rudderstack/rudder-integration-clevertap-react-native` | 2.0.0                  |
| `@rudderstack/rudder-integration-facebook-react-native`  | 1.4.0                  |
| `@rudderstack/rudder-integration-firebase-react-native`  | 2.4.0                  |
| `@rudderstack/rudder-integration-moengage-react-native`  | 1.5.0                  |
| `@rudderstack/rudder-integration-singular-react-native`  | 1.5.0                  |
| `@rudderstack/rudder-integration-sprig-react-native`     | 1.1.0                  |

CleverTap 2.0.0 is a major release: it needs iOS 15.0 or later. See its [upgrade notes](https://github.com/rudderlabs/rudder-sdk-react-native/tree/master/libs/rudder-integration-clevertap-react-native#upgrading-to-200).

To move your app from CocoaPods to SPM, run this from the app's root folder:

```
npx react-native spm add --deintegrate
```

This removes CocoaPods from the iOS project and links every React Native library through SPM. From then on, open `ios/<YourApp>.xcodeproj` in Xcode.

SPM generates its packages under `ios/build/`, which is gitignored. On a fresh clone or a CI checkout, run this after you install the npm dependencies and before you open Xcode or build:

```
npx react-native spm
```

Xcode resolves the packages before any build step runs, so without this the build stops at "Resolve Package Graph". To run it automatically, add `"postinstall": "react-native spm"` to the app's `package.json` scripts.

- SPM is all-or-nothing for React Native libraries: every one of them in your app needs a `Package.swift`.
- `spm add --deintegrate` removes CocoaPods from the whole app, including pods for your own native code. Their lines stay in `ios/Podfile`, but they are no longer linked. To keep them, remove the leftover React Native lines from the Podfile (such as the arguments of `use_react_native!(` and the `react_native_post_install(...)` call), run `pod install`, and open `ios/<YourApp>.xcworkspace` instead. Don't add `use_react_native!` back: React Native must come from either SPM or CocoaPods.
- Upgrade the SDK and the integrations together. Each integration's SPM package depends on the SDK's, so it needs SDK 3.3.0 or later.
- The DB encryption plugin (`@rudderstack/rudder-plugin-db-encryption-react-native`) supports CocoaPods only. With it installed, `spm add` stops with `Package.swift is missing`, so keep that app on CocoaPods.
- React Native's experimental remote SPM mode (`RN_SPM_REMOTE_URL`) isn't supported yet.
- To go back to CocoaPods:
  1. Remove the `react-native spm` `postinstall` script from `package.json`, if you added one. Otherwise every `npm install` runs it, and on a CocoaPods app it fails with `… is CocoaPods-integrated`.
  2. Run `npx react-native spm deinit`.
  3. Restore `ios/Podfile` from the commit before the SPM migration, for example with `git checkout <commit-before-migration> -- ios/Podfile`. `spm add --deintegrate` removed the React Native lines from it, `deinit` doesn't put them back, and `pod install` fails on what's left. Then add back any `pod` lines you added to the Podfile after the migration.
  4. Run `pod install` in the `ios` folder.

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

For more details, check out our [documentation](https://www.rudderstack.com/docs/sources/event-streams/sdks/rudderstack-react-native-sdk).

## Contact Us

If you come across any issues while configuring or using RudderStack React Native SDK, please feel free to start a conversation on our [Slack](https://resources.rudderstack.com/join-rudderstack-slack) channel. We will be happy to help you.
