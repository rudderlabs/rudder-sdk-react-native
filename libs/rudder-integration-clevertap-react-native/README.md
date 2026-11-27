# rudder-integration-clevertap-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-clevertap-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-clevertap-react-native`

### iOS

Run `pod install` in your app's `ios` folder. To use Swift Package Manager instead (React Native 0.87 or later), follow [Swift Package Manager (iOS)](https://github.com/rudderlabs/rudder-sdk-react-native/tree/master/libs/sdk#swift-package-manager-ios). It needs `@rudderstack/rudder-sdk-react-native` 3.3.0 or later.

## Upgrading to 2.0.0

Version 2.0.0 adds Swift Package Manager support. It moves the native integration from `Rudder-CleverTap` 1.1.x to 1.4.1 or later, which uses the CleverTap iOS SDK 7.1.1 instead of 4.2.

- Your app's iOS deployment target must be 15.0 or later. React Native 0.76 and later already require iOS 15.1.
- With CocoaPods, run `pod update Rudder-CleverTap CleverTap-iOS-SDK` in the `ios` folder. A plain `pod install` keeps the old versions from `Podfile.lock` and fails to resolve.
- Remove or raise any `Rudder-CleverTap` or `CleverTap-iOS-SDK` pins in your Podfile.
- If your app calls the CleverTap iOS SDK directly, check CleverTap's 5.x, 6.x and 7.x release notes for changes that affect you.

## Usage

```javascript
import RudderIntegrationCleverTapReactNative from '@rudderstack/rudder-integration-clevertap-react-native';

// TODO: What to do with the module?
RudderIntegrationCleverTapReactNative;
```
