// /**
//  * Sample React Native App
//  * https://github.com/facebook/react-native
//  *
//  * @format
//  * @flow strict-local
//  */

import * as React from 'react';
import {
  Text,
  Button,
} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import rc, { RUDDER_LOG_LEVEL } from '@rudderstack/rudder-sdk-react-native';
import clevertap from 'rudder-integration-clevertap-react-native'
import appsflyer from 'rudder-integration-appsflyer-react-native'
import { onAppOpenAttribution, onAttributionFailure, onDeepLink, onInstallConversionData, onInstallConversionFailure, registerForConversionListeners, registerForDeepLinkListeners } from 'rudder-integration-appsflyer-react-native/src/appsflyer';
const Stack = createNativeStackNavigator();
const initialization = async () => {
  const config = {
    dataPlaneUrl: 'https://cd88-175-101-36-4.ngrok.io',
    trackAppLifecycleEvents: true,
    autoCollectAdvertId:true,
    recordScreenViews: true,
    logLevel: RUDDER_LOG_LEVEL.VERBOSE,
    withFactories: [clevertap, appsflyer]
  };

  const props = {
    k1: 'v1',
    k2: 'v3',
    k3: 'v3',
    name: 'Miraj'
  };

  registerForConversionListeners();

  //registerForDeepLinkListeners();

  onAppOpenAttribution((data) => {
   console.log("On App Open Attribution Success and the data is ", data); 
  })

  onAttributionFailure((data) => {
    console.log("On App Attribution Failure and the data is ", data);
  })

  onInstallConversionData((data) => {
    console.log("On Install conversion Success data is ", data);
  })

  onInstallConversionFailure((data) => {
    console.log("On Install conversion Failure data is ", data);
  })

  onDeepLink((data) => {
    console.log("On Deeplink data is ", data);
  })

  await rc.setup('1pAKRv50y15Ti6UWpYroGJaO0Dj', config);

  await rc.identify("test_userIdiOS", {
    "email": "testuseriOS@example.com",
    "location": "UK"
  });
  await rc.track('React Native event', props);
  await rc.screen('React Native screen', props);
}

const App = () => {
  const routeNameRef = React.useRef();
  const navigationRef = React.useRef();

  

  (async function () {
    initialization()
  }

  )();
  return (
    <NavigationContainer
    ref={navigationRef}
    onReady={() => {
      routeNameRef.current = navigationRef.current.getCurrentRoute().name;
    }}
    onStateChange={async () => {
      const previousRouteName = routeNameRef.current;
      const currentRouteName = navigationRef.current.getCurrentRoute().name;

      if (previousRouteName !== currentRouteName) {
        await rc.screen(currentRouteName, {
          screen_name: currentRouteName,
          screen_class: currentRouteName,
        });
      }
      routeNameRef.current = currentRouteName;
    }}
    >
      <Stack.Navigator>
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          options={{ title: 'Welcome' }}
        />
        <Stack.Screen name="Profile" component={ProfileScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

const HomeScreen = ({ navigation }) => {
  (async function () {

    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj'
    };

    await rc.track('React Native Home Screen Event', props);
    await rc.screen('React Native  Home screen', props);
  }

  )();
  return (
    <><Button
      title="Go to Jane's profile"
      onPress={() => navigation.navigate('Profile', { name: 'Jane' })} />
      <Button
        title="Initialization"
        onPress={async () => await initialization()}/>
        </>
  );
};
const ProfileScreen = ({ navigation, route }) => {
  (async function () {

    const props = {
      k1: 'v1',
      k2: 'v3',
      k3: 'v3',
      name: 'Miraj'
    };

    await rc.track('React Native Profile Screen Event', props);
    await rc.screen('React Native  Profile screen', props);
  }

  )();
  return <Text>This is {route.params.name}'s profile</Text>;
};

export default App;