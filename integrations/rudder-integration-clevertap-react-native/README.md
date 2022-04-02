# rudder-integration-clevertap-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-clevertap-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-clevertap-react-native`

## Usage
```javascript
import RudderIntegrationCleverTapReactNative from '@rudderstack/rudder-integration-clevertap-react-native';

// TODO: What to do with the module?
RudderIntegrationCleverTapReactNative;
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
-keep class com.rudderstack.android.integrations.clevertap.** { *; }
```
