
# react-native-rudder-sdk

## Getting started

`$ npm install react-native-rudder-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-rudder-sdk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-rudder-sdk` and add `RNRudderSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNRudderSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNRudderSdkPackage;` to the imports at the top of the file
  - Add `new RNRudderSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-rudder-sdk'
  	project(':react-native-rudder-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-rudder-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-rudder-sdk')
  	```


## Usage
```javascript
import RNRudderSdk from 'react-native-rudder-sdk';

// TODO: What to do with the module?
RNRudderSdk;
```
  