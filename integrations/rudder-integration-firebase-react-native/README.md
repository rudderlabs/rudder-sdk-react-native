# rudder-integration-firebase-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-firebase-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-firebase-react-native`

## Usage
```javascript
import RudderIntegrationFirebaseReactNative from '@rudderstack/rudder-integration-firebase-react-native';

// TODO: What to do with the module?
RudderIntegrationFirebaseReactNative;
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
-keep com.rudderstack.android.integration.firebase.** { *; }
```
