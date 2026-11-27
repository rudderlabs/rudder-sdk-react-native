# rudder-plugin-db-encryption-react-native

## Getting started

`$ npm install @rudderstack/rudder-plugin-db-encryption-react-native --save`

Run `pod install` inside the `ios` directory of your project.

### iOS: CocoaPods only

This plugin supports CocoaPods only. It has no Swift Package Manager (SPM) support yet, so `npx react-native spm add` stops with `Package.swift is missing` in an app that has it installed.

- Keep an app that uses this plugin on CocoaPods.
- If you don't use the plugin, uninstall it before you move the app to SPM.
- To move an SPM app back to CocoaPods:
  1. Remove the `react-native spm` `postinstall` script from `package.json`, if you added one. Otherwise every `npm install` runs it, and on a CocoaPods app it fails with `… is CocoaPods-integrated`.
  2. Run `npx react-native spm deinit`.
  3. Restore `ios/Podfile` from the commit before the SPM migration, for example with `git checkout <commit-before-migration> -- ios/Podfile`. `spm add --deintegrate` removed the React Native lines from it, and `deinit` doesn't put them back. Then add back any `pod` lines you added to the Podfile after the migration.
  4. Run `pod install` in the `ios` folder.

## Usage

```javascript
import DBEncryption from '@rudderstack/rudder-plugin-db-encryption-react-native';
```
