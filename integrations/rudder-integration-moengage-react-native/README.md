# rudder-integration-moengage-react-native

## Getting started

`$ npm install @rudderstack/rudder-integration-moengage-react-native --save`

### Mostly automatic installation

`$ react-native link rudder-integration-moengage-react-native`


Run `pod install` inside the `ios` directory of your project adding `@rudderstack/rudder-integration-moengage-react-native` to your project.


## Usage
```javascript
import RudderIntegrationAppcenterReactNative from '@rudderstack/rudder-integration-moengage-react-native';
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
-keep com.rudderstack.android.integrations.moengage.** { *; }
```
