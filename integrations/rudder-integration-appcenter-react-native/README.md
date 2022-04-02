# rudder-integration-appcenter-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-appcenter-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-appcenter-react-native`


{% hint style="info" %}
  Make sure the `minSdkVersion` of your `build.gradle` in the root of `android` directory is atleast `21`
{% endhint %}

Run `pod install` inside the `ios` directory of your project adding `@rudderstack/rudder-integration-appcenter-react-native` to your project.


## Usage
```javascript
import RudderIntegrationAppcenterReactNative from '@rudderstack/rudder-integration-appcenter-react-native';

// TODO: What to do with the module?
RudderIntegrationAppcenterReactNative;
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
-keep class com.rudderstack.android.integrations.appcenter.** { *; }
```
