# rudder-integration-amplitude-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-amplitude-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-amplitude-react-native`


Run `pod install` inside the `ios` directory of your project adding `@rudderstack/rudder-integration-amplitude-react-native` to your project.


## Usage
```javascript
import RudderIntegrationAppcenterReactNative from '@rudderstack/rudder-integration-amplitude-react-native';
```

## Extra Step for minifyEnabled 

if you are using `minifyEnabled true` then you will have to use proguard

```
 buildTypes {
        debug {
           ...
        }
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
           
        }
    }
```


then you will have to use proguard `android/app/proguard-rules.pro`
```
-keep class com.rudderstack.android.integrations.amplitude.** { *; }
```
