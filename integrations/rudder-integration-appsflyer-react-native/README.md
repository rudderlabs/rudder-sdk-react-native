# rudder-integration-appsflyer-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-appsflyer-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-appsflyer-react-native`

## Usage
```javascript
import RudderIntegrationAppsflyerReactNative from '@rudderstack/rudder-integration-appsflyer-react-native';

// TODO: What to do with the module?
RudderIntegrationAppsflyerReactNative;
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
-keep class com.rudderstack.android.integrations.appsflyer.** { *; }
```
